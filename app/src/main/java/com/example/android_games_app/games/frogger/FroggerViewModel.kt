package com.example.android_games_app.games.frogger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_games_app.games.frogger.FroggerFixedValues.columnWidth
import com.example.android_games_app.games.frogger.FroggerFixedValues.defaultFrogXOffset
import com.example.android_games_app.games.frogger.FroggerFixedValues.gameRows
import com.example.android_games_app.games.frogger.FroggerFixedValues.leftMostBoundForRowObject
import com.example.android_games_app.games.frogger.FroggerFixedValues.rightMostBoundForRowObject
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowHeight
import com.example.android_games_app.games.frogger.FroggerFixedValues.yValueForLastRow
import com.example.android_games_app.games.frogger.utils.Frog
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

    private val roadSpeed = 2f
    private val riverSpeed = 3

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
            var collisionHappenedRightNow = false
            while (froggerGameState.value.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                delay(25)

                val updatedObjectOffsets = froggerGameState.value.objectXOffsets.toMutableList()
                for (laneNumber in 0 until updatedObjectOffsets.size) {
                    val currentLane = updatedObjectOffsets[laneNumber].toMutableList()
                    
                    for (objectNumber in 0 until currentLane.size) {
                        val objectXOffset = currentLane[objectNumber]
                        if (laneNumber == froggerGameState.value.frogCurrentRowIndex && !(froggerGameState.value.frogDiedOnRoad)) {
                            if (
                                collisionHappened(
                                    object1StartX = froggerGameState.value.frogXOffset,
                                    object1Width = columnWidth,
                                    object2StartX = objectXOffset,
                                    object2Width = if (gameRows[laneNumber].containsWiderObject) {
                                        columnWidth * 2
                                    } else {
                                        columnWidth
                                    }
                                )
                            ) {
                                collisionHappenedRightNow = true
                            }
                        }
                        currentLane[objectNumber] = if (gameRows[laneNumber].objectsAreGoingLeft) {
                            val newVal = objectXOffset - roadSpeed
                            if (newVal <= leftMostBoundForRowObject) {
                                rightMostBoundForRowObject - 1
                            } else {
                                newVal
                            }
                        }
                        else {
                            val newVal = objectXOffset + roadSpeed
                            if (newVal >= rightMostBoundForRowObject) {
                                leftMostBoundForRowObject + 1
                            } else {
                                newVal
                            }
                        }
                    }
                    updatedObjectOffsets[laneNumber] = currentLane
                }

                var updatedFrogDiedOnRoadValue = froggerGameState.value.frogDiedOnRoad
                var updatedFrogStatus = froggerGameState.value.frogStatus
                var updatedAnimCounter = froggerGameState.value.frogDiedOnRoadAnimationCounter
                var updatedFrogXOffset = froggerGameState.value.frogXOffset
                var updatedFrogYOffset = froggerGameState.value.frogYOffset

                var updatedFrogCurrentRowIndex = froggerGameState.value.frogCurrentRowIndex

                if (froggerGameState.value.frogDiedOnRoad) {
                    if (updatedAnimCounter == 0) {
                        updatedFrogStatus = Frog.FrogStatus.DEATH_BY_CAR_PHASE_1
                    } else if (updatedAnimCounter == 7) {
                        updatedFrogStatus = Frog.FrogStatus.DEATH_BY_CAR_PHASE_2
                    } else if (updatedAnimCounter == 14) {
                        updatedFrogStatus = Frog.FrogStatus.DEATH_BY_CAR_PHASE_3
                    } else if (updatedAnimCounter == 21) {
                        updatedFrogStatus = Frog.FrogStatus.DEATH_PHASE_FINAL
                    } else if (updatedAnimCounter == 28) {
                        updatedFrogStatus = Frog.FrogStatus.ALIVE_POINTING_UP
                        updatedFrogXOffset = defaultFrogXOffset
                        updatedFrogCurrentRowIndex = gameRows.size - 1
                        updatedFrogYOffset = getYOffsetForRowBasedOnIndex(gameRows.size - 1)
                        updatedFrogDiedOnRoadValue = false
                        updatedAnimCounter = -1
                    }
                    updatedAnimCounter += 1
                } else if (collisionHappenedRightNow) {
                    updatedFrogDiedOnRoadValue = true
                    collisionHappenedRightNow = false
                }

                froggerGameState.value = froggerGameState.value.copy(
                    objectXOffsets = updatedObjectOffsets,
                    frogStatus = updatedFrogStatus,
                    frogDiedOnRoad = updatedFrogDiedOnRoadValue,
                    frogDiedOnRoadAnimationCounter = updatedAnimCounter,
                    frogXOffset = updatedFrogXOffset,
                    frogYOffset = updatedFrogYOffset,
                    frogCurrentRowIndex = updatedFrogCurrentRowIndex
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

    fun onFrogHorizontalMovement(goingLeft: Boolean, screenWidth: Float) {
        if (froggerGameState.value.frogDiedOnRoad) {
            return
        }
        val newX = convertFloatToTwoDecimalPlaces(
            if (goingLeft) {
                froggerGameState.value.frogXOffset - columnWidth
            } else {
                froggerGameState.value.frogXOffset + columnWidth
            }
        )

        if (newX >= 0 && newX < screenWidth) {
            froggerGameState.value = froggerGameState.value.copy(
                frogXOffset = newX,
                frogStatus = if (goingLeft) {
                    Frog.FrogStatus.ALIVE_POINTING_LEFT
                } else {
                    Frog.FrogStatus.ALIVE_POINTING_RIGHT
                }
            )
        }
    }

    fun onFrogVerticalMovement(goingUp: Boolean) {
        if (froggerGameState.value.frogDiedOnRoad) {
            return
        }
        val newRow = if (goingUp) {
            froggerGameState.value.frogCurrentRowIndex - 1
        } else {
            froggerGameState.value.frogCurrentRowIndex + 1
        }

        if (newRow >= 0 && newRow < gameRows.size) {
            val newFrogYIndex = getYOffsetForRowBasedOnIndex(newRow)

            froggerGameState.value = froggerGameState.value.copy(
                frogYOffset = newFrogYIndex,
                frogCurrentRowIndex = newRow,
                frogStatus = if (goingUp) {
                    Frog.FrogStatus.ALIVE_POINTING_UP
                } else {
                    Frog.FrogStatus.ALIVE_POINTING_DOWN
                }

            )
        }
    }

    fun startNewGame() {
        stopGameLoop()

        froggerGameState.value = froggerGameState.value.copy(
            gameProgressStatus = GameProgressStatus.IN_PROGRESS,
            frogXOffset = defaultFrogXOffset,
            frogCurrentRowIndex = gameRows.size - 1,
            frogStatus = Frog.FrogStatus.ALIVE_POINTING_UP
        )
    }

    fun setValuesBasedOnScreenSize(
        screenWidth: Float,
        screenHeight: Float
    ) {
        leftMostBoundForRowObject = -(screenWidth * 0.5f)
        rightMostBoundForRowObject = screenWidth * 1.5f
        yValueForLastRow = convertFloatToTwoDecimalPlaces(screenHeight * 0.61f)
        columnWidth = convertFloatToTwoDecimalPlaces(screenWidth * .1f)
        rowHeight = convertFloatToTwoDecimalPlaces(screenHeight * .05f)

        var yOffsetValue = yValueForLastRow

        for (rowIndex in gameRows.size - 1 downTo 0) {
            gameRows[rowIndex].yOffsetValueOnScreen = yOffsetValue
            yOffsetValue -= rowHeight
        }

        val newOffsets: List<List<Float>> = listOf(
            emptyList(), // end zone
            emptyList(), // river row 1
            emptyList(), // river row 2
            emptyList(), // river row 3
            emptyList(), // river row 4
            emptyList(), // river row 5
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
            frogYOffset = getYOffsetForRowBasedOnIndex(gameRows.size - 1)
        )
    }
}

fun convertFloatToTwoDecimalPlaces(number: Float): Float =
    String.format(Locale.US, "%.2f", number).toFloat()

fun getYOffsetForRowBasedOnIndex(rowIndex: Int): Float {
    return yValueForLastRow - (((gameRows.size - 1) - rowIndex) * rowHeight)
}

fun collisionHappened(
    object1StartX: Float,
    object1Width: Float,
    object2StartX: Float,
    object2Width: Float
): Boolean {
    val object1EndX = object1StartX + object1Width
    val object2EndX = object2StartX + object2Width

    /**
     * Note to self: adding a .1 margin
     */

    val object1StartXWithMargin = object1StartX + (object1Width * .1)
    val object1EndXWithMargin = object1EndX - (object1Width * .1)

    return !((object1StartXWithMargin > object2EndX) || (object1EndXWithMargin < object2StartX))
}


