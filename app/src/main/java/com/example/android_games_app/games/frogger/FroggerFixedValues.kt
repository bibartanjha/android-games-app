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

    val gameRows: List<GameRow> = listOf(
        GameRow(
            rowType = GameRowType.RIVER
        ),
        GameRow(
            rowType = GameRowType.RIVER
        ),
        GameRow(
            rowType = GameRowType.RIVER
        ),
        GameRow(
            rowType = GameRowType.RIVER
        ),
        GameRow(
            rowType = GameRowType.RIVER
        ),
        GameRow(
            rowType = GameRowType.SAFE_ZONE
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectTypeInLane = RowObjectType.TRUCK,
            containsWiderObject = true
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = false,
            objectTypeInLane = RowObjectType.GRAY_RACE_CAR
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectTypeInLane = RowObjectType.PINK_CAR
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = false,
            objectTypeInLane = RowObjectType.BULLDOZER
        ),
        GameRow(
            rowType = GameRowType.ROAD,
            objectsAreGoingLeft = true,
            objectTypeInLane = RowObjectType.YELLOW_RACE_CAR
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
    var topBarHeight: Float = 100f
    var endZoneHeight: Float = 100f
    var rowHeight: Float = 100f
    var columnWidth: Float = 100f
}