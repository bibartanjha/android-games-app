package com.example.android_games_app.games.snake.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_games_app.games.snake.viewmodel.SnakeGameViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeScreen(
    onBackClicked: () -> Unit,
    snakeGameViewModel: SnakeGameViewModel
) {
    val gameState by snakeGameViewModel.getSnakeGameState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Snake")
                },
                navigationIcon = {
                    IconButton(onClick = {onBackClicked()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color(red = 215, green = 42, blue = 120, alpha = 255)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "This game is currently in development. Stay tuned!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Button(onClick = { snakeGameViewModel.updateIsPaused(false) }) {
                    Text("Play")
                }
                Button(onClick = { snakeGameViewModel.updateIsPaused(true) }) {
                    Text("Pause")
                }
                Button(onClick = { snakeGameViewModel.restartGame() }) {
                    Text("Restart")
                }
                Text(
                    text = gameState.currentNumber.toString()
                )

            }
        }
    }
}

@Preview
@Composable
fun SnakeScreenPreview() {
    SnakeScreen(
        onBackClicked = {},
        snakeGameViewModel = SnakeGameViewModel()
    )
}
