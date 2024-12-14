package com.example.android_games_app.games.wordle.model

import androidx.compose.ui.graphics.Color

data class LetterGuess (
    var currentLetter: String = "",
    var currentBGColor: Color = Color(red = 233, green = 218, blue = 193)
)