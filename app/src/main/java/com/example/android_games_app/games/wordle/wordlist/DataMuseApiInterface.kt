package com.example.android_games_app.games.wordle.wordlist

import retrofit2.http.GET
import retrofit2.http.Query

interface DataMuseApiInterface {
    @GET("/words")
    suspend fun getWords(
        @Query("sp") pattern: String,
        @Query("max") maxResults: Int = 100
    ): List<WordFromApiData>
}
