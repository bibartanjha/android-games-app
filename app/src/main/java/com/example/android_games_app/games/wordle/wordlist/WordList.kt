package com.example.android_games_app.games.wordle.wordlist

import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_LETTERS_IN_WORD

object WordList {
    private var wordList: List<String> = emptyList()

    fun getWordList(): List<String> = wordList

    suspend fun initializeWordList() {
        try {
            wordList = DataMuseApiService
                .getWords(NUM_LETTERS_IN_WORD, 500)
                .map { it.word.uppercase() }
            println("--- Word list initialized successfully")
        } catch (e: Exception) {
            println("--- Error initializing word list: ${e.message}")
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