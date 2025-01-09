package com.example.android_games_app.navigation

import androidx.compose.runtime.Composable
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
                wordleGameViewModel = wordleGameViewModel,
                snakeGameViewModel = snakeGameViewModel,
                twentyFortyEightGameViewModel = twentyFortyEightGameViewModel,
                froggerViewModel = froggerViewModel,
                onOptionSelected = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.WORDLE_SCREEN) {
            WordleGameScreen(
                wordleGameViewModel = wordleGameViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.SNAKE_SCREEN) {
            SnakeGameScreen(
                snakeGameViewModel = snakeGameViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.TWENTYFORTYEIGHT_SCREEN) {
            TwentyFortyEightGameScreen(
                twentyFortyEightGameViewModel = twentyFortyEightGameViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Routes.FROGGER_SCREEN) {
            FroggerScreen (
                froggerViewModel = froggerViewModel,
                onNavigateToOtherScreen = {
                    navController.navigate(it)
                }
            )
        }
    }
}