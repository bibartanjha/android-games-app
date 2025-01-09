package com.example.android_games_app.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.R
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.BaseCard
import com.example.android_games_app.utils.ImageCard

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
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp

            val optionCardWidth = screenWidth / 3

            val gameOptions = listOf(
                painterResource(R.drawable.wordle) to Routes.WORDLE_SCREEN,
                painterResource(R.drawable.snake) to Routes.SNAKE_SCREEN,
                painterResource(R.drawable.twentyfortyeight) to Routes.TWENTYFORTYEIGHT_SCREEN,
                painterResource(R.drawable.frogger_game_logo) to Routes.FROGGER_SCREEN,
            )

            for (option in gameOptions) {
                ImageCard(
                    cardWidth = optionCardWidth.dp,
                    cardHeight = optionCardWidth.dp,
                    painter = option.first,
                    onBaseCardClicked = {
                        onOptionSelected(option.second)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onOptionSelected = {})
}