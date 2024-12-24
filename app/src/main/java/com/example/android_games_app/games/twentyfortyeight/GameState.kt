package com.example.android_games_app.games.twentyfortyeight

import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE
import com.example.android_games_app.utils.GameProgressStatus

data class GameState (
    val gameGrid: List<MutableList<Int>> =
        List(DIM_SIZE) { MutableList(DIM_SIZE) { 0 } },
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED
)