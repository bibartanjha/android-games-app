package com.example.android_games_app.games.frogger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_games_app.games.frogger.FroggerFixedValues.animCounterReset
import com.example.android_games_app.games.frogger.FroggerFixedValues.columnWidth
import com.example.android_games_app.games.frogger.FroggerFixedValues.defaultFrogXOffset
import com.example.android_games_app.games.frogger.FroggerFixedValues.endZoneHeight
import com.example.android_games_app.games.frogger.FroggerFixedValues.endZoneAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.gameRows
import com.example.android_games_app.games.frogger.FroggerFixedValues.leftMostBoundForFrog
import com.example.android_games_app.games.frogger.FroggerFixedValues.leftMostBoundForRowObject
import com.example.android_games_app.games.frogger.FroggerFixedValues.rightMostBoundForFrog
import com.example.android_games_app.games.frogger.FroggerFixedValues.rightMostBoundForRowObject
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowHeight
import com.example.android_games_app.games.frogger.FroggerFixedValues.topBarAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.topBarHeight
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.games.frogger.utils.Frog.deathByCarPhases
import com.example.android_games_app.games.frogger.utils.GameRowType
import com.example.android_games_app.games.frogger.utils.RowObject
import com.example.android_games_app.utils.GameProgressStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class FroggerViewModel: ViewModel() {
    private val froggerGameState = MutableStateFlow(FroggerGameState())
    val getFroggerGameState: StateFlow<FroggerGameState> = froggerGameState
    private var job: Job? = null

    init {
        observeIsPausedState()
    }

//    private val roadSpeed = 2f
//    private val riverSpeed = 3f

    private val normalSpeedOffset = 2f
    private val fasterSpeedOffset = 2.5f

    private fun observeIsPausedState() {
        viewModelScope.launch {
            getFroggerGameState.collect { state ->
                if (state.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                    startGameLoop()
                } else {
                    stopGameLoop()
                }
            }
        }
    }

    private fun startGameLoop() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            while (froggerGameState.value.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                delay(25)

                var frogXOffset = froggerGameState.value.frogXOffset
                var frogAliveStatusValue = froggerGameState.value.frogAliveStatus
                var frogCurrentRowIndex = froggerGameState.value.frogCurrentRowIndex
                val objectOffsets = froggerGameState.value.objectXOffsets.toMutableList()

                var frogOnRiverObject = false

                for (rowIndex in 0 until gameRows.size) {
                    var row = gameRows[rowIndex]
                    var checkForCollisions = (rowIndex == frogCurrentRowIndex) && (froggerGameState.value.frogAliveStatus == Frog.FrogAliveStatus.ALIVE)
                    val rowObjectXOffsets = objectOffsets[rowIndex].toMutableList()

                    for (objectIndex in 0 until row.objectsInLane.size) {
                        val objectOffset = rowObjectXOffsets[objectIndex]
                        val objectWidth = columnWidth * row.numColumnsTakenUpByEachObject

                        val objectType = row.objectsInLane[objectIndex]

                        // note to self: had to do this to properly match the turtles png margins
                        val marginCollision = when (objectType) {
                            RowObject.RowObjectType.THREE_TURTLES -> 0.3
                            RowObject.RowObjectType.THREE_DIVING_TURTLES -> 0.3
                            RowObject.RowObjectType.TWO_TURTLES -> 0.4
                            RowObject.RowObjectType.TWO_DIVING_TURTLES -> 0.4
                            RowObject.RowObjectType.SHORT_LOG -> 0.2
                            RowObject.RowObjectType.MEDIUM_LOG -> 0.2
                            else -> 0.1
                        }
                        if (checkForCollisions &&
                            collisionHappened(
                                object1StartX = froggerGameState.value.frogXOffset,
                                object1Width = columnWidth,
                                object2StartX = objectOffset,
                                object2Width = objectWidth,
                                margin = marginCollision
                            )
                        ) {
                            when (row.rowType) {
                                GameRowType.ROAD -> frogAliveStatusValue = Frog.FrogAliveStatus.DEAD_ON_ROAD
                                GameRowType.RIVER -> {
                                    frogOnRiverObject = true
                                    // note to self: the frog width is the column width
                                    if (frogXOffset < (leftMostBoundForFrog - columnWidth) || frogXOffset >= (rightMostBoundForFrog + columnWidth)) {
                                        frogAliveStatusValue = Frog.FrogAliveStatus.DEAD_FROM_GOING_OUT_OF_BOUNDS
                                    }
                                    else if (gameRows[frogCurrentRowIndex].objectsAreGoingLeft) {
                                        frogXOffset -= row.speedInRow
                                    } else {
                                        frogXOffset += row.speedInRow
                                    }
                                }
                                else -> {}
                            }
                        }

                        // Moving row objects:
                        rowObjectXOffsets[objectIndex] = if (row.objectsAreGoingLeft) {
                            val newVal = objectOffset - row.speedInRow
                            if (newVal <= leftMostBoundForRowObject) {
                                rightMostBoundForRowObject - 1
                            } else {
                                newVal
                            }
                        } else {
                            val newVal = objectOffset + row.speedInRow
                            if (newVal >= rightMostBoundForRowObject) {
                                leftMostBoundForRowObject + 1
                            } else {
                                newVal
                            }
                        }
                    }

                    objectOffsets[rowIndex] = rowObjectXOffsets
                }

                // Note to self: putting this here to make sure whether frogOnRiverObject is still false after checking all the river rows
//                if (frogAliveStatusValue == Frog.FrogAliveStatus.ALIVE && gameRows[frogCurrentRowIndex].rowType == GameRowType.RIVER && (!frogOnRiverObject)) {
//                    frogAliveStatusValue = Frog.FrogAliveStatus.DEAD_ON_RIVER
//                }


                // Updating all other game values:
                var frogDisplayStatus = froggerGameState.value.frogDisplayStatus
                var frogDiedAnimCounter = froggerGameState.value.frogDiedAnimationCounter

                if (frogAliveStatusValue != Frog.FrogAliveStatus.ALIVE) {
                    if ((frogDiedAnimCounter/7) == 4) {
                        // next frog life:
                        frogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP
                        frogXOffset = defaultFrogXOffset
                        frogCurrentRowIndex = gameRows.size - 1
                        frogAliveStatusValue = Frog.FrogAliveStatus.ALIVE
                        frogDiedAnimCounter = -1
                    }

                    else if (frogDiedAnimCounter % 7 == 0) {
                        if (frogAliveStatusValue == Frog.FrogAliveStatus.DEAD_ON_ROAD) {
                            frogDisplayStatus = deathByCarPhases[frogDiedAnimCounter/7]
                        }
                    }

                    frogDiedAnimCounter += 1
                }

                val rowAnimCounter = if (froggerGameState.value.rowObjectAnimCounter == animCounterReset) {
                    0
                } else {
                    froggerGameState.value.rowObjectAnimCounter + 1
                }

                froggerGameState.value = froggerGameState.value.copy(
                    objectXOffsets = objectOffsets,
                    frogDisplayStatus = frogDisplayStatus,
                    frogAliveStatus = frogAliveStatusValue,
                    frogDiedAnimationCounter = frogDiedAnimCounter,
                    frogXOffset = frogXOffset,
                    frogCurrentRowIndex = frogCurrentRowIndex,
                    rowObjectAnimCounter = rowAnimCounter
                )
            }
        }
    }

    private fun stopGameLoop() {
        job?.cancel()
    }

    fun pauseGame() {
        froggerGameState.value = froggerGameState.value.copy(
            gameProgressStatus = GameProgressStatus.PAUSED
        )
    }

    fun resumeGame() {
        froggerGameState.value = froggerGameState.value.copy(
            gameProgressStatus = GameProgressStatus.IN_PROGRESS
        )
    }

    fun onFrogHorizontalMovement(goingLeft: Boolean, screenWidth: Float) {
        if (froggerGameState.value.frogAliveStatus != Frog.FrogAliveStatus.ALIVE) {
            return
        }
        val newX = reduceFloatDigits(
            floatToReduce = if (goingLeft) {
                froggerGameState.value.frogXOffset - columnWidth
            } else {
                froggerGameState.value.frogXOffset + columnWidth
            },
            numDigitsAfterDecimal = 2
        )

        if (newX >= 0 && newX < screenWidth) {
            froggerGameState.value = froggerGameState.value.copy(
                frogXOffset = newX,
                frogDisplayStatus = if (goingLeft) {
                    Frog.FrogDisplayStatus.POINTING_LEFT
                } else {
                    Frog.FrogDisplayStatus.POINTING_RIGHT
                }
            )
        }
    }

    fun onFrogVerticalMovement(goingUp: Boolean) {
        if (froggerGameState.value.frogAliveStatus != Frog.FrogAliveStatus.ALIVE) {
            return
        }
        val newRow = if (goingUp) {
            froggerGameState.value.frogCurrentRowIndex - 1
        } else {
            froggerGameState.value.frogCurrentRowIndex + 1
        }

        if (newRow >= 0 && newRow < gameRows.size) {
            froggerGameState.value = froggerGameState.value.copy(
                frogCurrentRowIndex = newRow,
                frogDisplayStatus = if (goingUp) {
                    Frog.FrogDisplayStatus.POINTING_UP
                } else {
                    Frog.FrogDisplayStatus.POINTING_DOWN
                }

            )
        }
    }

    fun startNewGame() {
        stopGameLoop()

        froggerGameState.value = froggerGameState.value.copy(
            gameProgressStatus = GameProgressStatus.IN_PROGRESS,
            frogXOffset = defaultFrogXOffset,
            frogCurrentRowIndex = gameRows.size - 7,
            frogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP
        )
    }

    fun setValuesBasedOnScreenSize(
        screenWidth: Float,
        screenHeight: Float
    ) {

        // Horizontal measurements/bounds
        leftMostBoundForRowObject = -(screenWidth * 0.5f)
        rightMostBoundForRowObject = screenWidth * 1.5f
        leftMostBoundForFrog = 0f
        rightMostBoundForFrog = screenWidth
        columnWidth = reduceFloatDigits(
            floatToReduce = screenWidth * .1f,
            numDigitsAfterDecimal = 2
        )

        // Vertical measurements/bounds
        endZoneHeight = screenHeight * endZoneAmountOfScreen
        rowHeight = screenHeight * rowAmountOfScreen
        topBarHeight = screenHeight * topBarAmountOfScreen

        var yOffsetValue = topBarHeight + endZoneHeight
        for (rowIndex in gameRows.indices) {
            gameRows[rowIndex].yOffsetValueForRow = yOffsetValue
            yOffsetValue += rowHeight
        }

        // X-coordinate values road and river objects
        val newOffsets: List<List<Float>> = listOf(
            listOf(
                screenWidth,
                (screenWidth/2),
                screenWidth * (3/2)
            ), // river row 1
            listOf(
                (-2) * (screenWidth/10),
                2 * (screenWidth/10),
                (6) *(screenWidth/10),
                screenWidth
            ), // river row 2
            listOf(
                0f,
                (6) *(screenWidth/10)
            ), // river row 3
            listOf(
                (-1)*(screenWidth/4),
                (screenWidth/4),
                screenWidth * (5/4)
            ), // river row 4
            listOf(
                0f,
                4 * (screenWidth/10),
                (8) *(screenWidth/10),
                (12) * (screenWidth/10)
            ), // river row 5
            emptyList(), // safe zone
            listOf(
                (-1)*(screenWidth/10),
                (4)*(screenWidth/10)
            ), // road row 1
            listOf(
                (-1)*(screenWidth/2),
                (-1)*(screenWidth/5),
                (1) *(screenWidth/10),
                (3) * (screenWidth/10)
            ), // road row 2
            listOf(
                (-2)*(screenWidth/10),
                (1)*(screenWidth/20),
                (4)*(screenWidth/10)
            ), // road row 3
            listOf(
                (-1)*(screenWidth/2),
                (-1)*(screenWidth/6),
                (screenWidth/6),
                (screenWidth/2)
            ), // road row 4
            listOf(
                (-1)*(screenWidth/4),
                (screenWidth/4),
                screenWidth * (5/4)
            ), // road row 5
            emptyList() // safe zone
        )

        froggerGameState.value = froggerGameState.value.copy(
            objectXOffsets = newOffsets,
        )
    }
}

fun reduceFloatDigits(floatToReduce: Float, numDigitsAfterDecimal: Int): Float =
    String.format(Locale.US, "%.${numDigitsAfterDecimal}f", floatToReduce).toFloat()

fun collisionHappened(
    object1StartX: Float,
    object1Width: Float,
    object2StartX: Float,
    object2Width: Float,
    margin: Double = 0.0,
): Boolean {
    val object1EndX = object1StartX + object1Width
    val object2EndX = object2StartX + object2Width

    val object2StartXWithMargin = object2StartX + (object2Width * margin)
    val object2EndXWithMargin = object2EndX - (object2Width * margin)

    return !((object1StartX > object2EndXWithMargin) || (object1EndX < object2StartXWithMargin))
}


