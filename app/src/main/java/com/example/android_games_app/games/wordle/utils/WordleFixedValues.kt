package com.example.android_games_app.games.wordle.utils

import androidx.compose.ui.graphics.Color

object WordleFixedValues {
    const val NUM_POSSIBLE_GUESSES = 6
    const val NUM_LETTERS_IN_WORD = 5
    val KEYBOARD_ROWS = listOf("QWERTYUIOP".toList(), "ASDFGHJKL".toList(), "ZXCVBNM".toList())
    val WORDLE_SCREEN_BG_COLOR: Color = Color(red = 158, green = 210, blue = 198)

}