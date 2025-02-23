package com.example.android_games_app.games.twentyfortyeight

import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE
import com.example.android_games_app.games.twentyfortyeight.utils.GridTile
import com.example.android_games_app.games.twentyfortyeight.utils.SwipeDirection
import com.example.android_games_app.utils.GameProgressStatus

data class TwentyFortyEightGameState (
    val gameGrid: List<MutableList<GridTile>> =
        List(DIM_SIZE) { MutableList(DIM_SIZE) {
            GridTile(
                value = 0
            )
        } },
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED,
    val mostRecentSwipeDirection: SwipeDirection = SwipeDirection.NONE
)