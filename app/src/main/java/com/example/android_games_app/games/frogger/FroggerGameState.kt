package com.example.android_games_app.games.frogger

import com.example.android_games_app.games.frogger.FroggerFixedValues.defaultFrogXOffset
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.utils.GameProgressStatus

data class FroggerGameState (
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED,
    val frogXOffset: Float = defaultFrogXOffset,
    val frogCurrentRowIndex: Int = FroggerFixedValues.gameRows.size - 1,
    val frogDisplayStatus: Frog.FrogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP,
    val objectXOffsets: List<List<Float>> = emptyList(),

    val frogAliveStatus: Frog.FrogAliveStatus = Frog.FrogAliveStatus.ALIVE,
    var frogDiedAnimationCounter: Int = 0,

    var rowObjectAnimCounter: Int = 0
)