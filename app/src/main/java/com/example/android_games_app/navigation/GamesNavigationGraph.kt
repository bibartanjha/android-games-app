package com.example.android_games_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_games_app.games.game3.Game3Screen
import com.example.android_games_app.games.snake.SnakeGameScreen
import com.example.android_games_app.games.snake.SnakeGameViewModel
import com.example.android_games_app.games.wordle.WordleGameScreen
import com.example.android_games_app.games.wordle.WordleGameViewModel
import com.example.android_games_app.homescreen.HomeScreen
import com.example.android_games_app.utils.GameProgressStatus

@Composable
fun GamesNavigationGraph(
    wordleGameViewModel: WordleGameViewModel,
    snakeGameViewModel: SnakeGameViewModel
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
                onPostGameOptionSelected = {
                    navController.navigate(it)
                },
                onBackClicked = {
                    navController.navigate(Routes.HOME_SCREEN)
                }
            )
        }

        composable(Routes.SNAKE_SCREEN) {
            SnakeGameScreen(
                snakeGameViewModel = snakeGameViewModel,
                onPostGameOptionSelected = {
                    navController.navigate(it)
                },
                onBackClicked = {
                    if (snakeGameViewModel.getSnakeGameState.value.gameProgressStatus in listOf(
                        GameProgressStatus.IN_PROGRESS, GameProgressStatus.NOT_STARTED
                    )) {
                        snakeGameViewModel.pauseGame()
                    }
                    navController.navigate(Routes.HOME_SCREEN)
                }
            )
        }

        composable(Routes.GAME_3_SCREEN) {
            Game3Screen(
                onBackClicked = {
                    navController.navigate(Routes.HOME_SCREEN)
                }
            )
        }
    }

}