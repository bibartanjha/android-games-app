package com.example.android_games_app.games.wordle.model

import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_LETTERS_IN_WORD
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_POSSIBLE_GUESSES

data class WordleGameState(
    var wordForUserToGuess: String = "",
    var lettersPlacements: MutableMap<Char, MutableSet<Int>> = HashMap(),
    val letterGuessValues: List<MutableList<LetterGuess>> =
        List(NUM_POSSIBLE_GUESSES) { MutableList(NUM_LETTERS_IN_WORD) { LetterGuess() } },
    val currentGuessNumber: Int = 0,
    val currentGuessLetterIndex: Int = 0,
    val gameInProgress: Boolean = false,
    val guessedWordSuccessfully: Boolean = false
)