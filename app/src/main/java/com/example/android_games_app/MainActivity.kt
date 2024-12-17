package com.example.android_games_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.android_games_app.games.ui.screens.GamesNavigationGraph
import com.example.android_games_app.games.wordle.wordlist.WordList
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        lifecycleScope.launch {
            WordList.initializeWordList() // doing this here because I want it only to be initialized once in the entire app lifecycle
        }

        setContent {
            GamesApp()
        }
    }

    @Composable
    fun GamesApp() {
        GamesNavigationGraph()
    }
}
