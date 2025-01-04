package com.example.android_games_app.games.frogger

import com.example.android_games_app.games.frogger.FroggerFixedValues.defaultFrogXOffset
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.utils.GameProgressStatus

data class FroggerGameState (
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED,
    val frogXOffset: Float = defaultFrogXOffset,
    val frogCurrentRowIndex: Int = FroggerFixedValues.gameRows.size - 1,
    val frogStatus: Frog.FrogStatus = Frog.FrogStatus.ALIVE_POINTING_UP,
    val objectXOffsets: List<List<Float>> = emptyList(),

    val frogDiedOnRoad: Boolean = false,
    var frogDiedOnRoadAnimationCounter: Int = 0
)