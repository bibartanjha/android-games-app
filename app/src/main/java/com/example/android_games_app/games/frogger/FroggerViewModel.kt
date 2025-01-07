package com.example.android_games_app.games.frogger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_games_app.games.frogger.FroggerFixedValues.animCounterReset
import com.example.android_games_app.games.frogger.FroggerFixedValues.defaultFrogXOffset
import com.example.android_games_app.games.frogger.FroggerFixedValues.frogHomesAmountOfScreenHeight
import com.example.android_games_app.games.frogger.FroggerFixedValues.frogAnimCounterInterval
import com.example.android_games_app.games.frogger.FroggerFixedValues.frogHomeWidth
import com.example.android_games_app.games.frogger.FroggerFixedValues.frogHomesStartIndices
import com.example.android_games_app.games.frogger.FroggerFixedValues.frogWidth
import com.example.android_games_app.games.frogger.FroggerFixedValues.gameRows
import com.example.android_games_app.games.frogger.FroggerFixedValues.leftMostBoundForFrog
import com.example.android_games_app.games.frogger.FroggerFixedValues.leftMostBoundForRowObject
import com.example.android_games_app.games.frogger.FroggerFixedValues.numFrogHomes
import com.example.android_games_app.games.frogger.FroggerFixedValues.numLivesDefault
import com.example.android_games_app.games.frogger.FroggerFixedValues.rightMostBoundForFrog
import com.example.android_games_app.games.frogger.FroggerFixedValues.rightMostBoundForRowObject
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowHeight
import com.example.android_games_app.games.frogger.FroggerFixedValues.topBarAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.yOffsetForFrogHomes
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.games.frogger.utils.Frog.deathOnRiverPhases
import com.example.android_games_app.games.frogger.utils.Frog.explosionPhases
import com.example.android_games_app.games.frogger.utils.FrogHome
import com.example.android_games_app.games.frogger.utils.GameRowType
import com.example.android_games_app.games.frogger.utils.RowObject.currentlyUnderwater
import com.example.android_games_app.games.frogger.utils.RowObject.getDisplayWidth
import com.example.android_games_app.games.frogger.utils.RowObject.getMarginForCollision
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
                var riverObjectThatFrogIsTravelingWith = froggerGameState.value.riverObjectThatFrogIsTravelingWith
                val objectOffsets = froggerGameState.value.objectXOffsets.toMutableList()


                for (rowIndex in 0 until gameRows.size) {
                    var row = gameRows[rowIndex]
                    val rowObjectXOffsets = objectOffsets[rowIndex].toMutableList()

                    var frogIsAliveAndInThisRow = (rowIndex == frogCurrentRowIndex) && (froggerGameState.value.frogAliveStatus == Frog.FrogAliveStatus.ALIVE)
                    var frogOnRiverObject = false

                    for (objectIndex in 0 until row.objectsInLane.size) {
                        val objectOffset = rowObjectXOffsets[objectIndex]
                        val objectWidth = row.objectsInLane[objectIndex].getDisplayWidth(frogWidth)
                        val marginCollision = row.objectsInLane[objectIndex].getMarginForCollision()

                        if (frogIsAliveAndInThisRow) {
                            if (gameRows[frogCurrentRowIndex].rowType == GameRowType.ROAD) {
                                if (collisionHappened(object1StartX = froggerGameState.value.frogXOffset, object1Width = frogWidth, object2StartX = objectOffset, object2Width = objectWidth, margin = marginCollision)) {
                                    frogAliveStatusValue = Frog.FrogAliveStatus.EXPLODED
                                }
                            } else if (gameRows[frogCurrentRowIndex].rowType == GameRowType.RIVER) {
                                val frogOnThisObject = (riverObjectThatFrogIsTravelingWith.first == rowIndex && riverObjectThatFrogIsTravelingWith.second == objectIndex)
                                if (frogOnThisObject) {
                                    frogOnRiverObject = !(row.objectsInLane[objectIndex].currentlyUnderwater(froggerGameState.value.rowObjectAnimCounter))
                                } else {
                                    val frogIsNotOnAnyObject = (riverObjectThatFrogIsTravelingWith.first == -1 && riverObjectThatFrogIsTravelingWith.second == -1)
                                    if (frogIsNotOnAnyObject) {
                                        if (collisionHappened(object1StartX = froggerGameState.value.frogXOffset, object1Width = frogWidth, object2StartX = objectOffset, object2Width = objectWidth, margin = marginCollision)) {
                                            frogOnRiverObject = true
                                            riverObjectThatFrogIsTravelingWith = Pair(rowIndex, objectIndex)
                                        }
                                    }
                                }
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

                    if (frogIsAliveAndInThisRow && row.rowType == GameRowType.RIVER) {
                        if (frogOnRiverObject) {
                            // move frog with the rest of the objects in the lane
                            if (frogXOffset < leftMostBoundForFrog || frogXOffset >= rightMostBoundForFrog) {
                                frogAliveStatusValue = Frog.FrogAliveStatus.DEAD_FROM_GOING_OUT_OF_BOUNDS
                            }
                            else if (gameRows[frogCurrentRowIndex].objectsAreGoingLeft) {
                                frogXOffset -= gameRows[frogCurrentRowIndex].speedInRow
                            } else {
                                frogXOffset += gameRows[frogCurrentRowIndex].speedInRow
                            }
                        } else {
                            frogAliveStatusValue = Frog.FrogAliveStatus.DROWNED
                        }
                    }
                }

                // Updating all other game values:
                var frogDisplayStatus = froggerGameState.value.frogDisplayStatus
                var frogDiedAnimCounter = froggerGameState.value.frogDiedAnimationCounter
                var numLivesLeft = froggerGameState.value.numLivesLeft
                var gameProgressStatus = froggerGameState.value.gameProgressStatus

                if (frogAliveStatusValue != Frog.FrogAliveStatus.ALIVE) {
                    if (frogDiedAnimCounter % frogAnimCounterInterval == 0) {
                        var reachedEndOfDyingAnimation = false
                        val indexExplosionPhase = frogDiedAnimCounter/frogAnimCounterInterval
                        if (frogAliveStatusValue == Frog.FrogAliveStatus.EXPLODED) {
                            if (indexExplosionPhase >= explosionPhases.size) {
                                reachedEndOfDyingAnimation = true
                            } else {
                                frogDisplayStatus = explosionPhases[indexExplosionPhase]
                            }
                        }
                        else if (frogAliveStatusValue == Frog.FrogAliveStatus.DROWNED) {
                            if (indexExplosionPhase >= deathOnRiverPhases.size) {
                                reachedEndOfDyingAnimation = true
                            } else {
                                frogDisplayStatus = deathOnRiverPhases[indexExplosionPhase]
                            }
                        } else {
                            reachedEndOfDyingAnimation = (frogDiedAnimCounter/frogAnimCounterInterval) == 4
                        }

                        if (reachedEndOfDyingAnimation) {
                            // next frog life:
                            frogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP
                            frogXOffset = defaultFrogXOffset
                            frogCurrentRowIndex = gameRows.size - 1
                            frogAliveStatusValue = Frog.FrogAliveStatus.ALIVE
                            frogDiedAnimCounter = -1
                            riverObjectThatFrogIsTravelingWith = Pair(-1, -1)
                            numLivesLeft -= 1
                            if (numLivesLeft == 0) {
                                gameProgressStatus = GameProgressStatus.LOST
                            }
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
                    rowObjectAnimCounter = rowAnimCounter,
                    riverObjectThatFrogIsTravelingWith = riverObjectThatFrogIsTravelingWith,
                    numLivesLeft = numLivesLeft,
                    gameProgressStatus = gameProgressStatus
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
                froggerGameState.value.frogXOffset - frogWidth
            } else {
                froggerGameState.value.frogXOffset + frogWidth
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
                },
                riverObjectThatFrogIsTravelingWith = Pair(-1, -1) // re-setting this will need to be checked again
            )
        }
    }

    fun onFrogVerticalMovement(goingUp: Boolean) {
        if (froggerGameState.value.frogAliveStatus != Frog.FrogAliveStatus.ALIVE) {
            return
        }

        val currentRow = froggerGameState.value.frogCurrentRowIndex

        if (currentRow == 0 && goingUp) { // reached homes
            var homeEntered = -1
            for (i in 0 until numFrogHomes) {
                if (
                    collisionHappened(
                        object1StartX = froggerGameState.value.frogXOffset,
                        object1Width = frogWidth,
                        object2StartX = frogHomesStartIndices[i],
                        object2Width = frogHomeWidth,
                        margin = 0.0
                    )
                ) {
                    homeEntered = i
                    break
                }
            }
            if (homeEntered == -1) {
                froggerGameState.value = froggerGameState.value.copy(
                    frogCurrentRowIndex = -1,
                    frogAliveStatus = Frog.FrogAliveStatus.EXPLODED
                )
            } else {
                val frogHomes = froggerGameState.value.frogHomes
                if (frogHomes[homeEntered].isOccupied) {
                    froggerGameState.value = froggerGameState.value.copy(
                        frogCurrentRowIndex = -1,
                        frogAliveStatus = Frog.FrogAliveStatus.EXPLODED
                    )
                } else {
                    frogHomes[homeEntered].isOccupied = true
                    var anyEmptyHomes = false
                    for (frogHome in frogHomes) {
                        if (!frogHome.isOccupied) {
                            anyEmptyHomes = true
                        }
                    }

                    val gameProgressStatus = if (anyEmptyHomes) {
                        froggerGameState.value.gameProgressStatus
                    } else {
                        GameProgressStatus.WON
                    }

                    froggerGameState.value = froggerGameState.value.copy(
                        frogHomes = frogHomes,
                        frogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP,
                        frogXOffset = defaultFrogXOffset,
                        frogCurrentRowIndex = gameRows.size - 1,
                        frogAliveStatus = Frog.FrogAliveStatus.ALIVE,
                        frogDiedAnimationCounter = 0,
                        riverObjectThatFrogIsTravelingWith = Pair(-1, -1),
                        gameProgressStatus = gameProgressStatus
                    )
                }
            }
        } else {
            val newRow = if (goingUp) {
                currentRow - 1
            } else {
                currentRow + 1
            }

            if (newRow >= 0 && newRow < gameRows.size) {
                froggerGameState.value = froggerGameState.value.copy(
                    frogCurrentRowIndex = newRow,
                    frogDisplayStatus = if (goingUp) {
                        Frog.FrogDisplayStatus.POINTING_UP
                    } else {
                        Frog.FrogDisplayStatus.POINTING_DOWN
                    },
                    riverObjectThatFrogIsTravelingWith = Pair(-1, -1) // re-setting this will need to be checked again
                )
            }
        }
    }

    fun startNewGame() {
        stopGameLoop()

        froggerGameState.value = froggerGameState.value.copy(
            gameProgressStatus = GameProgressStatus.IN_PROGRESS,
            frogXOffset = defaultFrogXOffset,
            frogCurrentRowIndex = gameRows.size - 1,
            frogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP,
            frogAliveStatus = Frog.FrogAliveStatus.ALIVE,
            frogDiedAnimationCounter = 0,
            rowObjectAnimCounter = 0,
            riverObjectThatFrogIsTravelingWith = Pair(-1, -1),
            frogHomes = List(numFrogHomes) {
                FrogHome(isOccupied = false)
            },
            numLivesLeft = numLivesDefault
        )
    }

    fun setValuesBasedOnScreenSize(
        screenWidth: Float,
        screenHeight: Float
    ) {
        // Frog homes:
        val homeBaseImageWidth = (screenWidth * .2).toFloat()
        val frogHomeStartOffsetInImage = (homeBaseImageWidth/12)
        val frogHomeEndOffsetInImage = homeBaseImageWidth * (46f/76f)
        frogHomeWidth = frogHomeEndOffsetInImage - frogHomeStartOffsetInImage
        for (i in 0 until frogHomesStartIndices.size) {
            frogHomesStartIndices[i] = (homeBaseImageWidth * i) + frogHomeStartOffsetInImage
        }


        // Horizontal measurements/bounds
        leftMostBoundForRowObject = -(screenWidth * 0.5f)
        rightMostBoundForRowObject = screenWidth * 1.5f
        frogWidth = reduceFloatDigits(
            floatToReduce = screenWidth * .1f,
            numDigitsAfterDecimal = 2
        )
        leftMostBoundForFrog = (-1) * frogWidth
        rightMostBoundForFrog = screenWidth + frogWidth

        // Vertical measurements/bounds
        rowHeight = screenHeight * rowAmountOfScreen

        var yOffsetValue = (screenHeight * topBarAmountOfScreen) + (screenHeight * frogHomesAmountOfScreenHeight)
        for (rowIndex in gameRows.indices) {
            gameRows[rowIndex].yOffsetValueForRow = yOffsetValue
            yOffsetValue += rowHeight
        }
        
        yOffsetForFrogHomes = (screenHeight * topBarAmountOfScreen) + (screenHeight * frogHomesAmountOfScreenHeight * 0.3f)

        // X-coordinate values road and river objects
        val newOffsets: List<List<Float>> = listOf(
            listOf(
                0f,
                (screenWidth/2),
                screenWidth
            ), // river row 1
            listOf(
                (-2) * (screenWidth/10),
                2 * (screenWidth/10),
                (6) *(screenWidth/10),
                screenWidth
            ), // river row 2
            listOf(
                0f,
                (8) *(screenWidth/10)
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


