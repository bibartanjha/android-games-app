package com.example.android_games_app.games.snake

import com.example.android_games_app.games.snake.utils.PointOnGameBoard
import com.example.android_games_app.games.snake.utils.SnakeDirection
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_COLS
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_ROWS
import com.example.android_games_app.utils.GameProgressStatus
import kotlin.random.Random

data class SnakeGameState(
    val snakeCoordinates: List<PointOnGameBoard> = listOf(
        PointOnGameBoard(Random.nextInt(NUM_GRID_ROWS), Random.nextInt(NUM_GRID_COLS))
    ),
    val snakeFoodCoordinates: PointOnGameBoard =
        PointOnGameBoard(Random.nextInt(NUM_GRID_ROWS), Random.nextInt(NUM_GRID_COLS)),
    val currentSnakeDirection: SnakeDirection = SnakeDirection.NONE,
    val currentScore: Int = 100,
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED
)
