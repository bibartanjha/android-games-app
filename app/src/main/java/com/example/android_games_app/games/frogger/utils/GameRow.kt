package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.games.frogger.utils.RowObject.RowObjectType

data class GameRow(
    val rowType: GameRowType,
    val objectsAreGoingLeft: Boolean = false,
    val objectsInLane: List<RowObjectType> = emptyList(),
    val speedInRow: Float = 2f,
    var yOffsetValueForRow: Float = 0f,
)
