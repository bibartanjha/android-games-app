package com.example.android_games_app.games.wordle.wordlist

import android.util.Log
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_LETTERS_IN_WORD

object WordList {
    private var wordList: List<String> = emptyList()

    suspend fun initializeWordList() {
        try {
            wordList = DataMuseApiService
                .getWords(NUM_LETTERS_IN_WORD, 500)
                .map { it.word.uppercase() }
            Log.d("Wordle Log", "--- Word list initialized successfully")
        } catch (e: Exception) {
            Log.d("Wordle Log", "--- Error initializing word list: ${e.message}")
        }
    }

    fun getRandomWordFromList(): String {
        if (wordList.isEmpty()) {
            val message = "Word list is empty"
            throw IllegalStateException(message)
        }
        return wordList.random()
    }
}