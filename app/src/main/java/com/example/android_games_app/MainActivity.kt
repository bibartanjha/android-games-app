package com.example.android_games_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_games_app.games.frogger.FroggerViewModel
import com.example.android_games_app.games.snake.SnakeGameViewModel
import com.example.android_games_app.games.twentyfortyeight.TwentyFortyEightGameViewModel
import com.example.android_games_app.navigation.GamesNavigationGraph
import com.example.android_games_app.games.wordle.WordleGameViewModel
import com.example.android_games_app.games.wordle.wordlist.WordList
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        initializeDataForGames()

        setContent {
            GamesApp()
        }
    }

    private fun initializeDataForGames() {
        lifecycleScope.launch {
            WordList.initializeWordList() // doing this here because I want it only to be initialized once in the entire app lifecycle
        }
    }

    @Composable
    fun GamesApp() {
        // note to self: creating the view model here so that even if the user goes back to the main screen, then their guesses get saved
        val wordleGameViewModel: WordleGameViewModel = viewModel()
        val snakeGameViewModel: SnakeGameViewModel = viewModel()
        val twentyFortyEightGameViewModel: TwentyFortyEightGameViewModel = viewModel()
        val froggerViewModel: FroggerViewModel = viewModel()

        GamesNavigationGraph(
            wordleGameViewModel,
            snakeGameViewModel,
            twentyFortyEightGameViewModel,
            froggerViewModel
        )
    }
}
