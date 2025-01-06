package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R
import com.example.android_games_app.games.frogger.FroggerFixedValues.numRowAnimPhases

object RowObject {
    enum class RowObjectType {
        YELLOW_RACE_CAR,
        BULLDOZER,
        GRAY_RACE_CAR,
        PINK_CAR,
        TRUCK,
        SHORT_LOG,
        MEDIUM_LOG,
        LONG_LOG,
        THREE_TURTLES,
        TWO_TURTLES,
        THREE_DIVING_TURTLES,
        TWO_DIVING_TURTLES
    }

    private fun getSpeedForRowObjectType(rowObjectType: RowObjectType): Float {
        return when (rowObjectType) {
            RowObjectType.LONG_LOG -> 2.5f
            else -> 2f
        }
    }

    private val threeTurtlePhases = listOf(
        R.drawable.frogger_three_turtles_phase_one,
        R.drawable.frogger_three_turtles_phase_two,
        R.drawable.frogger_three_turtles_phase_three
    )

    private val twoTurtlePhases = listOf(
        R.drawable.frogger_two_turtles_phase_one,
        R.drawable.frogger_two_turtles_phase_two,
        R.drawable.frogger_two_turtles_phase_three
    )

    val objectToImageMap = hashMapOf(
        RowObjectType.TRUCK to List(numRowAnimPhases) { R.drawable.frogger_truck },
        RowObjectType.GRAY_RACE_CAR to List(numRowAnimPhases) { R.drawable.frogger_gray_racecar },
        RowObjectType.PINK_CAR to List(numRowAnimPhases) { R.drawable.frogger_pink_car },
        RowObjectType.BULLDOZER to List(numRowAnimPhases) { R.drawable.frogger_bulldozer },
        RowObjectType.YELLOW_RACE_CAR to List(numRowAnimPhases) { R.drawable.frogger_yellow_racecar },
        RowObjectType.SHORT_LOG to List(numRowAnimPhases) { R.drawable.frogger_log_short },
        RowObjectType.MEDIUM_LOG to List(numRowAnimPhases) { R.drawable.frogger_log_medium },
        RowObjectType.LONG_LOG to List(numRowAnimPhases) { R.drawable.frogger_log_long },
        RowObjectType.THREE_TURTLES to List(numRowAnimPhases/threeTurtlePhases.size) { threeTurtlePhases }.flatten(),
        RowObjectType.TWO_TURTLES to List(numRowAnimPhases/twoTurtlePhases.size) { twoTurtlePhases }.flatten()
    )

    fun getDefaultObjectImage(): Int = R.drawable.pacman
}


