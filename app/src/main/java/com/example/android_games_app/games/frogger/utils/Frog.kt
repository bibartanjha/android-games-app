package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R

object Frog {
    enum class FrogDisplayStatus {
        POINTING_DOWN,
        POINTING_UP,
        POINTING_LEFT,
        POINTING_RIGHT,
        DEATH_BY_CAR_PHASE_1,
        DEATH_BY_CAR_PHASE_2,
        DEATH_BY_CAR_PHASE_3,
        DEATH_PHASE_FINAL
    }

    val deathByCarPhases = listOf(
        FrogDisplayStatus.DEATH_BY_CAR_PHASE_1,
        FrogDisplayStatus.DEATH_BY_CAR_PHASE_2,
        FrogDisplayStatus.DEATH_BY_CAR_PHASE_3,
        FrogDisplayStatus.DEATH_PHASE_FINAL
    )

    val statusToImageMap = hashMapOf(
        FrogDisplayStatus.POINTING_LEFT to R.drawable.froggerleft,
        FrogDisplayStatus.POINTING_RIGHT to R.drawable.froggerright,
        FrogDisplayStatus.POINTING_UP to R.drawable.froggerup,
        FrogDisplayStatus.POINTING_DOWN to R.drawable.froggerdown,
        FrogDisplayStatus.DEATH_BY_CAR_PHASE_1 to R.drawable.frog_death_on_road_1,
        FrogDisplayStatus.DEATH_BY_CAR_PHASE_2 to R.drawable.frog_death_on_road_2,
        FrogDisplayStatus.DEATH_BY_CAR_PHASE_3 to R.drawable.frog_death_on_road_3,
        FrogDisplayStatus.DEATH_PHASE_FINAL to R.drawable.frog_death_final
    )

    fun getDefaultDirectionImage(): Int = R.drawable.froggerup

    enum class FrogAliveStatus {
        ALIVE, DEAD_ON_ROAD, DEAD_ON_RIVER, DEAD_FROM_GOING_OUT_OF_BOUNDS
    }
}