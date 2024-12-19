package com.example.android_games_app.games.snake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_games_app.games.snake.utils.PointOnGameBoard
import com.example.android_games_app.games.snake.utils.SnakeDirection
import com.example.android_games_app.games.snake.utils.SnakeDirectionMaps
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_COLS
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_ROWS
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.POINT_INCREMENT_VALUE
import com.example.android_games_app.utils.GameProgressStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class SnakeGameViewModel: ViewModel() {

    private val snakeGameState = MutableStateFlow(SnakeGameState())
    val getSnakeGameState: StateFlow<SnakeGameState> = snakeGameState
    private var job: Job? = null

    init {
        observeIsPausedState()
    }

    private fun observeIsPausedState() {
        viewModelScope.launch {
            getSnakeGameState.collect { state ->
                if (state.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                    startGameLoop()
                } else {
                    stopGameLoop()
                }
            }
        }
    }

    private fun startGameLoop() {
        // If the game loop is already running, don't start it again.
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            while (snakeGameState.value.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                delay(200)
                val updatedSnakeCoordinates = snakeGameState.value.snakeCoordinates.toMutableList()
                val head: PointOnGameBoard = updatedSnakeCoordinates[0]
                when (snakeGameState.value.currentSnakeDirection) {
                    SnakeDirection.DOWN -> {
                        updatedSnakeCoordinates.add(0, head.copy(
                                row = if (head.row == NUM_GRID_ROWS - 1) {
                                    0
                                } else {
                                    head.row + 1
                                }
                            )
                        )
                    }
                    SnakeDirection.UP -> {
                        updatedSnakeCoordinates.add(0, head.copy(
                                row = if (head.row == 0) {
                                    NUM_GRID_ROWS - 1
                                } else {
                                    head.row - 1
                                }
                            )
                        )
                    }
                    SnakeDirection.LEFT -> {
                        updatedSnakeCoordinates.add(0, head.copy(
                                col = if (head.col == 0) {
                                    NUM_GRID_COLS - 1
                                } else {
                                    head.col - 1
                                }
                            )
                        )
                    }
                    SnakeDirection.RIGHT -> {
                        updatedSnakeCoordinates.add(0, head.copy(
                                col = if (head.col == NUM_GRID_COLS - 1) {
                                    0
                                } else {
                                    head.col + 1
                                }
                            )
                        )
                    }
                    SnakeDirection.NONE -> {}
                }

                val collidedWithFood = (updatedSnakeCoordinates[0] == snakeGameState.value.snakeFoodCoordinates)

                if ((snakeGameState.value.currentSnakeDirection != SnakeDirection.NONE) && !collidedWithFood) {
                    updatedSnakeCoordinates.removeLast()
                }

                var collidedWithSelf = false
                for (ind in 1 until updatedSnakeCoordinates.size) {
                    if (updatedSnakeCoordinates[0] == updatedSnakeCoordinates[ind]) {
                        collidedWithSelf = true
                        break
                    }
                }

                snakeGameState.value = snakeGameState.value.copy(
                    snakeCoordinates = updatedSnakeCoordinates,
                    snakeFoodCoordinates = if (collidedWithFood) {
                        PointOnGameBoard(Random.nextInt(NUM_GRID_ROWS), Random.nextInt(NUM_GRID_COLS))
                    } else {
                        snakeGameState.value.snakeFoodCoordinates
                    },
                    currentScore = if (collidedWithFood) {
                        snakeGameState.value.currentScore + POINT_INCREMENT_VALUE
                    } else {
                        snakeGameState.value.currentScore
                    },
                    gameProgressStatus = if (collidedWithSelf) {
                        GameProgressStatus.LOST // in this game, there is no differentiation between winning or losing. just the game being over. so just put LOST
                    } else {
                        snakeGameState.value.gameProgressStatus
                    }
                )
            }
        }
    }

    private fun stopGameLoop() {
        job?.cancel()
    }

    fun updateSnakeDirection(newSnakeDirection: SnakeDirection) {
        var gameProgressStatus = snakeGameState.value.gameProgressStatus
        if (gameProgressStatus == GameProgressStatus.NOT_STARTED) {
            gameProgressStatus = GameProgressStatus.IN_PROGRESS
        }
        if (gameProgressStatus != GameProgressStatus.IN_PROGRESS) {
            return
        }
        val currentDirection = snakeGameState.value.currentSnakeDirection
        if (SnakeDirectionMaps.oppositeDirectionMap[currentDirection] == newSnakeDirection) {
            return
        }
        snakeGameState.value = snakeGameState.value.copy(
            currentSnakeDirection = newSnakeDirection,
            gameProgressStatus = gameProgressStatus
        )
    }

    fun pauseGame() {
        snakeGameState.value = snakeGameState.value.copy(
            gameProgressStatus = GameProgressStatus.PAUSED
        )
    }

    fun resumeGame() {
        snakeGameState.value = snakeGameState.value.copy(
            gameProgressStatus = GameProgressStatus.IN_PROGRESS
        )
    }

    fun startNewGame() {
        stopGameLoop()
        snakeGameState.value = snakeGameState.value.copy(
            snakeCoordinates = listOf(
                PointOnGameBoard(Random.nextInt(NUM_GRID_ROWS), Random.nextInt(NUM_GRID_COLS))
            ),
            snakeFoodCoordinates = PointOnGameBoard(Random.nextInt(NUM_GRID_ROWS), Random.nextInt(NUM_GRID_COLS)),
            currentSnakeDirection = SnakeDirection.NONE,
            currentScore = 100,
            gameProgressStatus = GameProgressStatus.NOT_STARTED
        )
    }
}