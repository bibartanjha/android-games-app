package com.example.android_games_app.games.wordle.model

data class GameFinishStatus (
    var gameFinished: Boolean,
    var guessedWordSuccessfully: Boolean,
    val correctWord: String,
    val numGuessesMade: Int
)