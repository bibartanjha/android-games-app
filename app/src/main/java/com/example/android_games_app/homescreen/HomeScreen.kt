package com.example.android_games_app.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_games_app.R
import com.example.android_games_app.games.frogger.FroggerViewModel
import com.example.android_games_app.games.snake.SnakeGameViewModel
import com.example.android_games_app.games.twentyfortyeight.TwentyFortyEightGameViewModel
import com.example.android_games_app.games.wordle.WordleGameViewModel
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.GameProgressStatus
import com.example.android_games_app.utils.ImageCard

@Composable
fun HomeScreen(
    wordleGameViewModel: WordleGameViewModel,
    snakeGameViewModel: SnakeGameViewModel,
    twentyFortyEightGameViewModel: TwentyFortyEightGameViewModel,
    froggerViewModel: FroggerViewModel,
    onOptionSelected: (optionName: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(red = 255, green = 203, blue = 90)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp

            val optionCardWidth = screenWidth / 3

            ImageCard(
                cardWidth = optionCardWidth.dp,
                cardHeight = optionCardWidth.dp,
                painter = painterResource(R.drawable.wordle),
                onBaseCardClicked = {
                    if (wordleGameViewModel.getWordleGameState.value.gameProgressStatus == GameProgressStatus.NOT_STARTED) {
                        wordleGameViewModel.startNewGame()
                    }
                    onOptionSelected(Routes.WORDLE_SCREEN)
                }
            )

            ImageCard(
                cardWidth = optionCardWidth.dp,
                cardHeight = optionCardWidth.dp,
                painter = painterResource(R.drawable.snake),
                onBaseCardClicked = {
                    if (snakeGameViewModel.getSnakeGameState.value.gameProgressStatus == GameProgressStatus.NOT_STARTED) {
                        snakeGameViewModel.startNewGame()
                    }
                    onOptionSelected(Routes.SNAKE_SCREEN)
                }
            )

            ImageCard(
                cardWidth = optionCardWidth.dp,
                cardHeight = optionCardWidth.dp,
                painter = painterResource(R.drawable.twentyfortyeight),
                onBaseCardClicked = {
                    if (twentyFortyEightGameViewModel.getTwentyFortyEightGameState.value.gameProgressStatus == GameProgressStatus.NOT_STARTED) {
                        twentyFortyEightGameViewModel.startNewGame()
                    }
                    onOptionSelected(Routes.TWENTYFORTYEIGHT_SCREEN)
                }
            )

            ImageCard(
                cardWidth = optionCardWidth.dp,
                cardHeight = optionCardWidth.dp,
                painter = painterResource(R.drawable.frogger_game_logo),
                onBaseCardClicked = {
                    if (froggerViewModel.getFroggerGameState.value.gameProgressStatus == GameProgressStatus.NOT_STARTED) {
                        froggerViewModel.startNewGame()
                    }
                    onOptionSelected(Routes.FROGGER_SCREEN)
                }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        wordleGameViewModel = WordleGameViewModel(),
        snakeGameViewModel = SnakeGameViewModel(),
        twentyFortyEightGameViewModel = TwentyFortyEightGameViewModel(),
        froggerViewModel = FroggerViewModel(),
        onOptionSelected = {}
    )
}