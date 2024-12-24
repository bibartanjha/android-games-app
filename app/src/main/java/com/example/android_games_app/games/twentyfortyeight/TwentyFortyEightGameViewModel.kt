package com.example.android_games_app.games.twentyfortyeight

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE
import com.example.android_games_app.games.twentyfortyeight.utils.GridFunctions
import com.example.android_games_app.games.twentyfortyeight.utils.SwipeDirection
import com.example.android_games_app.utils.GameProgressStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class TwentyFortyEightGameViewModel: ViewModel() {

    private val gameState = MutableStateFlow(GameState())
    val getGameState: StateFlow<GameState> = gameState

    fun userMove(direction: SwipeDirection) {
        val gameStateValue = gameState.value

        val updatedGrid = when (direction) {
            SwipeDirection.LEFT -> GridFunctions.shiftGridLeft(gameStateValue.gameGrid)
            SwipeDirection.RIGHT -> GridFunctions.shiftGridRight(gameStateValue.gameGrid)
            SwipeDirection.UP -> GridFunctions.shiftGridUp(gameStateValue.gameGrid)
            SwipeDirection.DOWN -> GridFunctions.shiftGridDown(gameStateValue.gameGrid)
            SwipeDirection.NONE -> gameStateValue.gameGrid
        }

        var updatedGameStatus = gameStateValue.gameProgressStatus

        val tilesWith2048 = GridFunctions.getAllTilesWithValue(updatedGrid, 2048)
        if (tilesWith2048.isNotEmpty()) {
            updatedGameStatus = GameProgressStatus.WON
        } else {
            val tilesWithZero = GridFunctions.getAllTilesWithValue(updatedGrid, 0)
            if (tilesWithZero.isEmpty() && (!GridFunctions.containsAdjacentTilesWithEqualValue(updatedGrid))) {
                updatedGameStatus = GameProgressStatus.LOST
            } else {
                val randomTile = tilesWithZero.random()
                updatedGrid[randomTile.first][randomTile.second] = 2
            }
        }

//        for (row in updatedGrid.getTiles().indices) {
//            Log.d("2048 Log", "--- Row $row: ${updatedGrid.getTiles()[row]}")
//        }

        gameState.value = gameStateValue.copy(
            gameGrid = updatedGrid,
            gameProgressStatus = updatedGameStatus
        )
    }

    fun startNewGame() {
        val newGrid = List(DIM_SIZE) { MutableList(DIM_SIZE) { 0 } }
        newGrid[Random.nextInt(DIM_SIZE)][Random.nextInt(DIM_SIZE)] = 2


        gameState.value = gameState.value.copy(
            gameGrid = newGrid,
            gameProgressStatus = GameProgressStatus.NOT_STARTED
        )
    }

}