package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowAnimCounterIntervalLength

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

    fun RowObjectType.getDisplayWidth(frogWidth: Float): Float =
        when (this) {
            RowObjectType.YELLOW_RACE_CAR -> frogWidth
            RowObjectType.BULLDOZER -> frogWidth
            RowObjectType.GRAY_RACE_CAR -> frogWidth
            RowObjectType.PINK_CAR -> frogWidth
            RowObjectType.TRUCK -> frogWidth * 2
            RowObjectType.SHORT_LOG -> frogWidth * 2
            RowObjectType.MEDIUM_LOG -> frogWidth * 2.78f
            RowObjectType.LONG_LOG -> frogWidth * 4.22f
            RowObjectType.THREE_TURTLES -> frogWidth * 2
            RowObjectType.TWO_TURTLES -> frogWidth * 2
            RowObjectType.THREE_DIVING_TURTLES -> frogWidth * 2
            RowObjectType.TWO_DIVING_TURTLES -> frogWidth * 2
        }

    // note to self: had to do this to properly offset any margins within the pngs
    fun RowObjectType.getMarginForCollision(): Double =
        when (this) {
            RowObjectType.SHORT_LOG -> 0.2
            RowObjectType.MEDIUM_LOG -> 0.2
            RowObjectType.THREE_TURTLES -> 0.3
            RowObjectType.TWO_TURTLES -> 0.4
            RowObjectType.THREE_DIVING_TURTLES -> 0.3
            RowObjectType.TWO_DIVING_TURTLES -> 0.4
            else -> 0.1
        }

    private val threeTurtlePhases = listOf(
        R.drawable.frogger_three_turtles_flippers_forward,
        R.drawable.frogger_three_turtles_flippers_back,
        R.drawable.frogger_three_turtles_flippers_in
    )

    private val threeDivingTurtlePhases = listOf(
        R.drawable.frogger_three_turtles_flippers_forward,
        R.drawable.frogger_three_turtles_flippers_back,
        R.drawable.frogger_three_diving_turtles_phase_partially_in_water,
        R.drawable.frogger_three_diving_turtles_phase_almost_fully_in_water,
        R.drawable.frogger_three_diving_turtles_phase_fully_in_water,
        R.drawable.frogger_three_diving_turtles_phase_partially_in_water
    )

    private val twoTurtlePhases = listOf(
        R.drawable.frogger_two_turtles_flippers_forward,
        R.drawable.frogger_two_turtles_flippers_back,
        R.drawable.frogger_two_turtles_flippers_in
    )

    private val twoDivingTurtlePhases = listOf(
        R.drawable.frogger_two_turtles_flippers_forward,
        R.drawable.frogger_two_turtles_flippers_back,
        R.drawable.frogger_two_diving_turtles_phase_partially_in_water,
        R.drawable.frogger_two_diving_turtles_phase_almost_fully_in_water,
        R.drawable.frogger_two_diving_turtles_phase_fully_in_water,
        R.drawable.frogger_two_diving_turtles_phase_partially_in_water
    )

    private fun currAnimCounterInterval(rowObjectAnimCounter: Int) = rowObjectAnimCounter / rowAnimCounterIntervalLength

    fun RowObjectType.getImage(rowObjectAnimCounter: Int = 0): Int =
        when (this) {
            RowObjectType.YELLOW_RACE_CAR -> R.drawable.frogger_yellow_racecar
            RowObjectType.BULLDOZER -> R.drawable.frogger_bulldozer
            RowObjectType.GRAY_RACE_CAR -> R.drawable.frogger_gray_racecar
            RowObjectType.PINK_CAR -> R.drawable.frogger_pink_car
            RowObjectType.TRUCK -> R.drawable.frogger_truck
            RowObjectType.SHORT_LOG -> R.drawable.frogger_log_short
            RowObjectType.MEDIUM_LOG -> R.drawable.frogger_log_medium
            RowObjectType.LONG_LOG -> R.drawable.frogger_log_long
            RowObjectType.THREE_TURTLES -> {
                threeTurtlePhases[currAnimCounterInterval(rowObjectAnimCounter) % threeTurtlePhases.size]
            }
            RowObjectType.TWO_TURTLES -> {
                twoTurtlePhases[currAnimCounterInterval(rowObjectAnimCounter) % twoTurtlePhases.size]
            }
            RowObjectType.THREE_DIVING_TURTLES -> {
                threeDivingTurtlePhases[currAnimCounterInterval(rowObjectAnimCounter)]
            }
            RowObjectType.TWO_DIVING_TURTLES -> {
                twoDivingTurtlePhases[currAnimCounterInterval(rowObjectAnimCounter)]
            }
        }

    fun RowObjectType.currentlyUnderwater(rowObjectAnimCounter: Int = 0): Boolean =
        when (this) {
            RowObjectType.THREE_DIVING_TURTLES ->
                threeDivingTurtlePhases[currAnimCounterInterval(rowObjectAnimCounter)] ==
                        R.drawable.frogger_three_diving_turtles_phase_fully_in_water
            RowObjectType.TWO_DIVING_TURTLES ->
                twoDivingTurtlePhases[currAnimCounterInterval(rowObjectAnimCounter)] ==
                        R.drawable.frogger_two_diving_turtles_phase_fully_in_water
            else -> false
        }
}


