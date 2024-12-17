package com.example.android_games_app.games.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_games_app.games.ui.screens.games.Game2
import com.example.android_games_app.games.ui.screens.games.Game3
import com.example.android_games_app.games.wordle.view.WordleGameScreen
import com.example.android_games_app.games.wordle.viewmodel.WordleGameViewModel
import com.example.android_games_app.games.wordle.wordlist.WordList

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
                    // if the user is in the middle of the game and went back
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
            Game2()
        }

        composable(Routes.GAME_3_SCREEN) {
            Game3()
        }
    }

}