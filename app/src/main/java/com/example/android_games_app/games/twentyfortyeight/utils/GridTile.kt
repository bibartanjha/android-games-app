package com.example.android_games_app.games.twentyfortyeight.utils

data class GridTile(
    var value: Int = 0,
    var position: Pair<Int, Int> = Pair(0, 0),
    var hadRecentMerge: Boolean = false,
    var isNewTile: Boolean = false
)
