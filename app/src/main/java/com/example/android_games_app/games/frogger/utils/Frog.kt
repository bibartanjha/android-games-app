package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R

object Frog {
    enum class FrogStatus {
        ALIVE_POINTING_DOWN,
        ALIVE_POINTING_UP,
        ALIVE_POINTING_LEFT,
        ALIVE_POINTING_RIGHT,
        DEATH_BY_CAR_PHASE_1,
        DEATH_BY_CAR_PHASE_2,
        DEATH_BY_CAR_PHASE_3,
        DEATH_PHASE_FINAL
    }

    val statusToImageMap = hashMapOf(
        FrogStatus.ALIVE_POINTING_LEFT to R.drawable.froggerleft,
        FrogStatus.ALIVE_POINTING_RIGHT to R.drawable.froggerright,
        FrogStatus.ALIVE_POINTING_UP to R.drawable.froggerup,
        FrogStatus.ALIVE_POINTING_DOWN to R.drawable.froggerdown,
        FrogStatus.DEATH_BY_CAR_PHASE_1 to R.drawable.frog_death_on_road_1,
        FrogStatus.DEATH_BY_CAR_PHASE_2 to R.drawable.frog_death_on_road_2,
        FrogStatus.DEATH_BY_CAR_PHASE_3 to R.drawable.frog_death_on_road_3,
        FrogStatus.DEATH_PHASE_FINAL to R.drawable.frog_death_final
    )

    fun getDefaultDirectionImage(): Int = R.drawable.froggerup
}