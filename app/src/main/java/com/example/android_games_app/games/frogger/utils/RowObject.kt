package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R

object RowObject {
    enum class RowObjectType {
        YELLOW_CAR,
        BULLDOZER,
        RACE_CAR,
        PINK_CAR,
        TRUCK,
        NONE
    }

    val objectToImageMap = hashMapOf(
        RowObjectType.TRUCK to R.drawable.frogger_truck,
        RowObjectType.RACE_CAR to R.drawable.frogger_racecar,
        RowObjectType.PINK_CAR to R.drawable.frogger_pink_car,
        RowObjectType.BULLDOZER to R.drawable.frogger_bulldozer,
        RowObjectType.YELLOW_CAR to R.drawable.frogger_yellow_car
    )

    fun getDefaultObjectImage(): Int = R.drawable.pacman
}


