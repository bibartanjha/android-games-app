package com.example.android_games_app.games.snake.utils

import androidx.compose.ui.graphics.Color

object SnakeFixedValues {
    const val NUM_GRID_ROWS = 30
    const val NUM_GRID_COLS = 20
    const val POINT_INCREMENT_VALUE = 100
    val SNAKE_GAME_SCREEN_BG_COLOR: Color = Color(red = 215, green = 42, blue = 120, alpha = 255)
    val delayIntervals: List<Long> = listOf(400, 300, 200, 100, 50)
}