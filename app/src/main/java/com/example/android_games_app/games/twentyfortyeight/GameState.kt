package com.example.android_games_app.games.twentyfortyeight

import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE
import com.example.android_games_app.games.twentyfortyeight.utils.GridTile
import com.example.android_games_app.utils.GameProgressStatus

data class GameState (
    val gameGrid: List<MutableList<GridTile>> =
        List(DIM_SIZE) { MutableList(DIM_SIZE) {
            GridTile(
                value = 0,
                position = it to it
            )
        } },
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED
)