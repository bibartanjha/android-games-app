package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R

object Frog {
    enum class FrogDisplayStatus {
        POINTING_DOWN,
        POINTING_UP,
        POINTING_LEFT,
        POINTING_RIGHT,
        DEATH_ON_ROAD_PHASE_1,
        DEATH_ON_ROAD_PHASE_2,
        DEATH_ON_ROAD_PHASE_3,
        DEATH_ON_RIVER_PHASE_1,
        DEATH_ON_RIVER_PHASE_2,
        DEATH_ON_RIVER_PHASE_3,
        DEATH_PHASE_FINAL
    }

    val explosionPhases = listOf(
        FrogDisplayStatus.DEATH_ON_ROAD_PHASE_1,
        FrogDisplayStatus.DEATH_ON_ROAD_PHASE_2,
        FrogDisplayStatus.DEATH_ON_ROAD_PHASE_3,
        FrogDisplayStatus.DEATH_PHASE_FINAL
    )

    val deathOnRiverPhases = listOf(
        FrogDisplayStatus.DEATH_ON_RIVER_PHASE_1,
        FrogDisplayStatus.DEATH_ON_RIVER_PHASE_2,
        FrogDisplayStatus.DEATH_ON_RIVER_PHASE_3,
        FrogDisplayStatus.DEATH_PHASE_FINAL
    )

    fun FrogDisplayStatus.getImageId(): Int {
        return when (this) {
            FrogDisplayStatus.POINTING_LEFT -> R.drawable.froggerleft
            FrogDisplayStatus.POINTING_DOWN -> R.drawable.froggerdown
            FrogDisplayStatus.POINTING_UP -> R.drawable.froggerup
            FrogDisplayStatus.POINTING_RIGHT -> R.drawable.froggerright
            FrogDisplayStatus.DEATH_ON_ROAD_PHASE_1 -> R.drawable.frog_death_explosion_phase_1
            FrogDisplayStatus.DEATH_ON_ROAD_PHASE_2 -> R.drawable.frog_death_explosion_phase_2
            FrogDisplayStatus.DEATH_ON_ROAD_PHASE_3 -> R.drawable.frog_death_explosion_phase_3
            FrogDisplayStatus.DEATH_ON_RIVER_PHASE_1 -> R.drawable.frogger_drown_phase_1
            FrogDisplayStatus.DEATH_ON_RIVER_PHASE_2 -> R.drawable.frogger_drown_phase_2
            FrogDisplayStatus.DEATH_ON_RIVER_PHASE_3 -> R.drawable.frogger_drown_phase_3
            FrogDisplayStatus.DEATH_PHASE_FINAL -> R.drawable.frog_death_final
        }
    }

    enum class FrogAliveStatus {
        ALIVE, EXPLODED, DROWNED, DEAD_FROM_GOING_OUT_OF_BOUNDS
    }
}