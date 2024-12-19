package com.example.android_games_app.games.snake.utils

enum class SnakeDirection {
    DOWN,
    UP,
    LEFT,
    RIGHT,
    NONE
}

object SnakeDirectionMaps {
    val oppositeDirectionMap = hashMapOf(
        SnakeDirection.LEFT to SnakeDirection.RIGHT,
        SnakeDirection.RIGHT to SnakeDirection.LEFT,
        SnakeDirection.UP to SnakeDirection.DOWN,
        SnakeDirection.DOWN to SnakeDirection.UP,
        SnakeDirection.NONE to SnakeDirection.NONE,
    )
}