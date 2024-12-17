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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.ui.BaseCard

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
            BaseCard(
                textValue = "Select Game",
                cardWidth = 300.dp,
                cardHeight = 50.dp,
                backgroundColor = Color.DarkGray,
                textColor = Color.White,
                textSize = 24.sp
            )
            val optionCardWidth = 130.dp
            val optionCardHeight = 130.dp

            BaseCard(
                textValue = Routes.WORDLE_SCREEN,
                cardWidth = optionCardWidth,
                cardHeight = optionCardHeight,
                textSize = 18.sp,
                onBaseCardClicked = {
                    onOptionSelected(Routes.WORDLE_SCREEN)
                }
            )

            BaseCard(
                textValue = Routes.GAME_2_SCREEN,
                cardWidth = optionCardWidth,
                cardHeight = optionCardHeight,
                textSize = 18.sp,
                onBaseCardClicked = {
                    onOptionSelected(Routes.GAME_2_SCREEN)
                }
            )

            BaseCard(
                textValue = Routes.GAME_3_SCREEN,
                cardWidth = optionCardWidth,
                cardHeight = optionCardHeight,
                textSize = 18.sp,
                onBaseCardClicked = {
                    onOptionSelected(Routes.GAME_3_SCREEN)
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