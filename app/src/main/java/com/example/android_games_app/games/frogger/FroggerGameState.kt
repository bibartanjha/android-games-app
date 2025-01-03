package com.example.android_games_app.games.frogger

import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.utils.GameProgressStatus

data class FroggerGameState (
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED,
    val frogXOffset: Float = 0f,
    val frogYOffset: Float = 0f,
    val frogCurrentRowIndex: Int = FroggerFixedValues.gameRows.size - 1,
    val frogDirection: Frog.FrogDirection = Frog.FrogDirection.UP,
    val objectXOffsets: List<List<Float>> = emptyList()
)