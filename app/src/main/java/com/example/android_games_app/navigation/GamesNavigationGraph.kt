package com.example.android_games_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_games_app.games.frogger.FroggerScreen
import com.example.android_games_app.games.frogger.FroggerViewModel
import com.example.android_games_app.games.snake.SnakeGameScreen
import com.example.android_games_app.games.snake.SnakeGameViewModel
import com.example.android_games_app.games.twentyfortyeight.TwentyFortyEightGameScreen
import com.example.android_games_app.games.twentyfortyeight.TwentyFortyEightGameViewModel
import com.example.android_games_app.games.wordle.WordleGameScreen
import com.example.android_games_app.games.wordle.WordleGameViewModel
import com.example.android_games_app.homescreen.HomeScreen
import com.example.android_games_app.utils.GameProgressStatus

@Composable
fun GamesNavigationGraph(
    wordleGameViewModel: WordleGameViewModel,
    snakeGameViewModel: SnakeGameViewModel,
    twentyFortyEightGameViewModel: TwentyFortyEightGameViewModel,
    froggerViewModel: FroggerViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {
        composable(Routes.HOME_SCREEN) {
            HomeScreen(
                onOptionSelected = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.WORDLE_SCREEN) {
            LaunchedEffect(Unit) {
                if (wordleGameViewModel.getWordleGameState.value.gameProgressStatus != GameProgressStatus.IN_PROGRESS) {
                    /**
                    If the user is in the middle of the game and went back to the main menu,
                    and then clicks back on Wordle, then we don't want to start a new game
                     Instead, we want the previous game state to be kept. That is why I added this if-check
                     */
                    wordleGameViewModel.startNewGame()
                }
            }
            WordleGameScreen(
                wordleGameViewModel = wordleGameViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.SNAKE_SCREEN) {
            LaunchedEffect(Unit) {
                if (snakeGameViewModel.getSnakeGameState.value.gameProgressStatus !in listOf(
                        GameProgressStatus.IN_PROGRESS, GameProgressStatus.PAUSED)) {
                    /**
                    If the user is in the middle of the game and went back to the main menu,
                    and then clicks back on Snake, then we don't want to start a new game
                    Instead, we want the previous game state to be kept. That is why I added this if-check
                     */
                    snakeGameViewModel.startNewGame()
                }
            }
            SnakeGameScreen(
                snakeGameViewModel = snakeGameViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.TWENTYFORTYEIGHT_SCREEN) {
            LaunchedEffect(Unit) {
                twentyFortyEightGameViewModel.startNewGame()
            }
            TwentyFortyEightGameScreen(
                twentyFortyEightGameViewModel = twentyFortyEightGameViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.FROGGER_SCREEN) {
            LaunchedEffect(Unit) {
                if (froggerViewModel.getFroggerGameState.value.gameProgressStatus !in listOf(
                        GameProgressStatus.IN_PROGRESS, GameProgressStatus.PAUSED)) {
                    froggerViewModel.startNewGame()
                }
            }
            FroggerScreen (
                froggerViewModel = froggerViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }
    }

}