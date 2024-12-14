package com.example.android_games_app.games.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_games_app.games.ui.HeaderCard
import com.example.android_games_app.games.ui.OptionCard

@Composable
fun HomeScreen(
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
            HeaderCard(
                headerText = "Select Game",
                backgroundColor = Color.DarkGray,
                textColor = Color.White
            )
            OptionCard(
                optionName = "Wordle",
                onOptionCardClicked = {
                    onOptionSelected("Wordle")
                }
            )
            OptionCard(
                optionName = "Game2",
                onOptionCardClicked = {
                    onOptionSelected("Game2")
                }
            )
            OptionCard(
                optionName = "Game3",
                onOptionCardClicked = {
                    onOptionSelected("Game3")
                }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onOptionSelected = {})
}