package com.example.android_games_app.games.wordle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.R
import com.example.android_games_app.games.wordle.utils.LetterGuessTextView
import com.example.android_games_app.games.wordle.utils.TextInputButton
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.BUTTON_COLOR
import com.example.android_games_app.utils.TopBarWithBackIcon
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.KEYBOARD_ROWS
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_LETTERS_IN_WORD
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_POSSIBLE_GUESSES
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.WORDLE_SCREEN_BG_COLOR
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.GamePauseOrCompleteScreen
import com.example.android_games_app.utils.GameProgressStatus

@Composable
fun WordleGameScreen(
    wordleGameViewModel: WordleGameViewModel,
    onNavigateToOtherScreen: (optionName: String) -> Unit,
) {
    val gameState by wordleGameViewModel.getWordleGameState.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBackIcon(
                gameName = "Wordle",
                onIconClicked = {
                    onNavigateToOtherScreen(Routes.HOME_SCREEN)
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = WORDLE_SCREEN_BG_COLOR
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp
                val screenHeight = configuration.screenHeightDp

                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        if (gameState.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                            wordleGameViewModel.restartPressed()
                        } else {
                            wordleGameViewModel.startNewGame()
                        }
                    }) {
                        Text("Start New Game")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val horizontalSpacingBetweenLetters = 4
                val boxSize = (screenWidth/6.5).toInt()
                val verticalSpacingBetweenLetters = 8

                Box {
                    Column {
                        for (row in 0 until NUM_POSSIBLE_GUESSES) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(horizontalSpacingBetweenLetters.dp, Alignment.CenterHorizontally)
                            ) {
                                for (col in 0 until NUM_LETTERS_IN_WORD) {
                                    LetterGuessTextView(
                                        backgroundColor = gameState.letterGuessValues[row][col].currentBGColor,
                                        letterInView = gameState.letterGuessValues[row][col].currentLetter,
                                        boxSize = boxSize
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(verticalSpacingBetweenLetters.dp))
                        }
                    }

                    if (gameState.gameProgressStatus in listOf(GameProgressStatus.WON, GameProgressStatus.LOST)) {
                        val overlayWidth = ((NUM_LETTERS_IN_WORD - 1) * horizontalSpacingBetweenLetters) + (NUM_LETTERS_IN_WORD * boxSize)
                        val overlayHeight = ((NUM_POSSIBLE_GUESSES - 1) * verticalSpacingBetweenLetters) + (NUM_POSSIBLE_GUESSES * boxSize)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(horizontalSpacingBetweenLetters.dp, Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(overlayWidth.dp)
                                    .height(overlayHeight.dp)
                                    .background(
                                        color = Color.Yellow.copy(
                                            alpha = 0.5f
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (gameState.gameProgressStatus == GameProgressStatus.WON) "You Win!" else gameState.wordForUserToGuess,
                                    color = Color.Black,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                for (keyboardRow in KEYBOARD_ROWS.indices) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        val specialButtonDimension = (screenWidth/6.85).dp
                        val specialButtonFontSize = (screenWidth/25.68).sp
                        if (keyboardRow == KEYBOARD_ROWS.size - 1) { // add BACK button to last row
                            TextInputButton(
                                textInButton = "ENTER",
                                buttonClicked = {
                                    wordleGameViewModel.handleEnter()
                                },
                                buttonWidth = specialButtonDimension,
                                buttonHeight = specialButtonDimension,
                                fontSize = specialButtonFontSize
                            )
                        }

                        for (letter in KEYBOARD_ROWS[keyboardRow]) {
                            TextInputButton(
                                textInButton = letter.toString(),
                                buttonClicked = {
                                    wordleGameViewModel.addLetter(it)
                                },
                                buttonWidth = (screenWidth/12.8).dp,
                                buttonHeight = (screenHeight/13.125).dp,
                                fontSize = (screenWidth/15.74).sp
                            )
                        }

                        if (keyboardRow == KEYBOARD_ROWS.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(specialButtonDimension)
                                    .height(specialButtonDimension)
                                    .background(
                                        color = BUTTON_COLOR,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        wordleGameViewModel.removeLetter()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.keyboard_back_button),
                                    modifier = Modifier.fillMaxSize(0.75f),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            if (gameState.gameProgressStatus == GameProgressStatus.RESTART_PRESSED) {
                GamePauseOrCompleteScreen(
                    text = "Are you sure you want to start a new game?",
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Restart", "Cancel"),
                    onButtonSelection = {
                        if (it == "Restart") {
                            wordleGameViewModel.startNewGame()
                        } else if (it == "Cancel") {
                            wordleGameViewModel.resumeGame()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun WordleGameScreenPreview() {
    WordleGameScreen(
        wordleGameViewModel = WordleGameViewModel(),
        onNavigateToOtherScreen = {}
    )
}