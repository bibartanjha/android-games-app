package com.example.android_games_app.games.frogger

import androidx.compose.ui.graphics.Color
import com.example.android_games_app.games.frogger.utils.GameRow
import com.example.android_games_app.games.frogger.utils.GameRowType
import com.example.android_games_app.games.frogger.utils.RowObject.RowObjectType

object FroggerFixedValues {

    val FROGGER_SCREEN_BG_COLOR: Color = Color.Black
    const val defaultFrogXOffset: Float = 0f
    const val topBarAmountOfScreen: Float = 0.07f
    const val frogHomesAmountOfScreenHeight: Float = 0.07f
    const val rowAmountOfScreen: Float = 0.04f

    const val rowAnimCounterIntervalLength: Int = 10
    const val numRowAnimPhases: Int = 6
    const val animCounterReset = (rowAnimCounterIntervalLength * numRowAnimPhases) - 1

    const val frogAnimCounterInterval: Int = 10

    const val numFrogHomes: Int = 5


    val gameRows: List<GameRow> = listOf(
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.MEDIUM_LOG,
                RowObjectType.MEDIUM_LOG,
                RowObjectType.MEDIUM_LOG
            )
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_DIVING_TURTLES,
                RowObjectType.THREE_TURTLES
            )
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.LONG_LOG,
                RowObjectType.LONG_LOG
            ),
            speedInRow = 3f
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.SHORT_LOG,
                RowObjectType.SHORT_LOG,
                RowObjectType.SHORT_LOG
            )
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_DIVING_TURTLES,
                RowObjectType.THREE_TURTLES
            )
        ),
        GameRow(
            rowType = GameRowType.SAFE_ZONE
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(RowObjectType.TRUCK, RowObjectType.TRUCK)
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.GRAY_RACE_CAR,
                RowObjectType.GRAY_RACE_CAR,
                RowObjectType.GRAY_RACE_CAR,
                RowObjectType.GRAY_RACE_CAR
            )
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.PINK_CAR,
                RowObjectType.PINK_CAR,
                RowObjectType.PINK_CAR
            )
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.BULLDOZER,
                RowObjectType.BULLDOZER,
                RowObjectType.BULLDOZER,
                RowObjectType.BULLDOZER
            )
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.YELLOW_RACE_CAR,
                RowObjectType.YELLOW_RACE_CAR,
                RowObjectType.YELLOW_RACE_CAR
            )
        ),
        GameRow(
            rowType = GameRowType.SAFE_ZONE
        ),
    )

    /**
     * Note to self: These variables are going to be reset by the Screen via a LaunchedEffect because they are dependent on the screen size
     * However, I am putting them in FroggerFixedValues instead of FroggerGameState because, once these values are set, they set constant for the rest of the game play
     */
    var leftMostBoundForRowObject = Float.NEGATIVE_INFINITY
    var rightMostBoundForRowObject = Float.POSITIVE_INFINITY
    var leftMostBoundForFrog = Float.NEGATIVE_INFINITY
    var rightMostBoundForFrog = Float.POSITIVE_INFINITY
    var rowHeight: Float = 100f
    var frogWidth: Float = 100f

    var frogHomeWidth = 100f
    val frogHomesStartIndices: MutableList<Float> = MutableList(5) { 0f }
    var yOffsetForFrogHomes = 100f
}