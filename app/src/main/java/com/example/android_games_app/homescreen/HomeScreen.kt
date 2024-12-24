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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.BaseCard

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
            val screenHeight = configuration.screenHeightDp

            val headerWidth = screenWidth / 1.37
            val headerHeight = screenHeight / 16.8
            val headerTextSize = (headerWidth/12.5)
            BaseCard(
                textValue = "Select Game",
                cardWidth = headerWidth.dp,
                cardHeight = headerHeight.dp,
                backgroundColor = Color.DarkGray,
                textColor = Color.White,
                textSize = headerTextSize.sp
            )
            val optionCardWidth = screenWidth / 3
            val optionCardTextSize = (optionCardWidth/7.22)

            val gameOptions = listOf(
                Routes.WORDLE_SCREEN,
                Routes.SNAKE_SCREEN,
                Routes.TWENTYFORTYEIGHT_SCREEN
            )

            for (option in gameOptions) {
                BaseCard(
                    textValue = option,
                    cardWidth = optionCardWidth.dp,
                    cardHeight = optionCardWidth.dp,
                    textSize = optionCardTextSize.sp,
                    onBaseCardClicked = {
                        onOptionSelected(option)
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