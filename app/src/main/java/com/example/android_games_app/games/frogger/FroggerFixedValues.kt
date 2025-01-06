package com.example.android_games_app.games.frogger

import androidx.compose.ui.graphics.Color
import com.example.android_games_app.games.frogger.utils.GameRow
import com.example.android_games_app.games.frogger.utils.GameRowType
import com.example.android_games_app.games.frogger.utils.RowObject.RowObjectType

object FroggerFixedValues {

    val FROGGER_SCREEN_BG_COLOR: Color = Color.Black
    val defaultFrogXOffset: Float = 0f
    val topBarAmountOfScreen: Float = 0.07f
    val endZoneAmountOfScreen: Float = 0.07f
    val rowAmountOfScreen: Float = 0.04f

    val rowAnimCounterInterval: Int = 10
    val numRowAnimPhases: Int = 6
    val animCounterReset = (rowAnimCounterInterval * numRowAnimPhases) - 1

    val gameRows: List<GameRow> = listOf(
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.MEDIUM_LOG,
                RowObjectType.MEDIUM_LOG,
                RowObjectType.MEDIUM_LOG
            ),
            numColumnsTakenUpByEachObject = 2.78f
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.TWO_TURTLES,
                RowObjectType.TWO_TURTLES,
                RowObjectType.TWO_TURTLES,
                RowObjectType.TWO_TURTLES
            ),
            numColumnsTakenUpByEachObject = 2f
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.LONG_LOG,
                RowObjectType.LONG_LOG
            ),
            speedInRow = 2.5f,
            numColumnsTakenUpByEachObject = 4.22f
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.SHORT_LOG,
                RowObjectType.SHORT_LOG,
                RowObjectType.SHORT_LOG
            ),
            numColumnsTakenUpByEachObject = 2f
        ),
        GameRow(
            rowType = GameRowType.RIVER,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_TURTLES,
                RowObjectType.THREE_TURTLES
            ),
            numColumnsTakenUpByEachObject = 2f
        ),
        GameRow(
            rowType = GameRowType.SAFE_ZONE
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(RowObjectType.TRUCK, RowObjectType.TRUCK),
            numColumnsTakenUpByEachObject = 2f
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.GRAY_RACE_CAR,
                RowObjectType.GRAY_RACE_CAR,
                RowObjectType.GRAY_RACE_CAR,
                RowObjectType.GRAY_RACE_CAR
            ),
            numColumnsTakenUpByEachObject = 1f
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.PINK_CAR,
                RowObjectType.PINK_CAR,
                RowObjectType.PINK_CAR
            ),
            numColumnsTakenUpByEachObject = 1f
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = false,
            objectsInLane = listOf(
                RowObjectType.BULLDOZER,
                RowObjectType.BULLDOZER,
                RowObjectType.BULLDOZER,
                RowObjectType.BULLDOZER
            ),
            numColumnsTakenUpByEachObject = 1f
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectsInLane = listOf(
                RowObjectType.YELLOW_RACE_CAR,
                RowObjectType.YELLOW_RACE_CAR,
                RowObjectType.YELLOW_RACE_CAR
            ),
            numColumnsTakenUpByEachObject = 1f
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
    var topBarHeight: Float = 100f
    var endZoneHeight: Float = 100f
    var rowHeight: Float = 100f
    var columnWidth: Float = 100f
}