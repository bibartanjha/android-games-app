package com.example.android_games_app.games.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_games_app.games.game2.Game2Screen
import com.example.android_games_app.games.game3.Game3Screen
import com.example.android_games_app.games.wordle.view.WordleGameScreen
import com.example.android_games_app.games.wordle.viewmodel.WordleGameViewModel

@Composable
fun GamesNavigationGraph(
    wordleGameViewModel: WordleGameViewModel
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
                if (!wordleGameViewModel.getWordleGameState.value.gameInProgress) {
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
                }
            )
        }

        composable(Routes.GAME_2_SCREEN) {
            Game2Screen()
        }

        composable(Routes.GAME_3_SCREEN) {
            Game3Screen()
        }
    }

}