package com.example.android_games_app.games.wordle.wordlist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataMuseApiService {
    suspend fun getWords(
        numLettersInWord: Int,
        maxWordsToFetch: Int
    ): List<WordFromApiData> {
        return withContext(Dispatchers.IO) {
            val apiService = Retrofit.Builder()
                .baseUrl("https://api.datamuse.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DataMuseApiInterface::class.java)

            val pattern = "?".repeat(numLettersInWord)
            val response = apiService.getWords(pattern, maxWordsToFetch) // Note for self: Adding five question-marks means we're requesting 5-letter words
            if (response.isEmpty()) {
                val message = "Failed to fetch words from DataMuse API"
                throw Exception(message)
            }
            response
        }
    }
}