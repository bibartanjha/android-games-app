package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.games.frogger.utils.RowObject.RowObjectType

data class GameRow(
    val rowType: GameRowType,
    val objectsAreGoingLeft: Boolean = false,
    val objectTypeInLane: RowObjectType = RowObjectType.NONE,
    val containsWiderObject: Boolean = false,
    var yOffsetValueOnScreen: Float = 0f
)
