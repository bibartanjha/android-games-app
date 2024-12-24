package com.example.android_games_app.games.twentyfortyeight

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE
import com.example.android_games_app.games.twentyfortyeight.utils.GridFunctions
import com.example.android_games_app.games.twentyfortyeight.utils.GridTile
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

//        for (row in updatedGrid) {
//            Log.d("2048 Log", "--- UpdatedGrid: $updatedGrid")
//        }

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
                updatedGrid[randomTile.first][randomTile.second].value = 2
                updatedGrid[randomTile.first][randomTile.second] =
                    GridTile(value = 2, position = Pair(randomTile.first, randomTile.second))
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
        val newGrid = List(DIM_SIZE) { MutableList(DIM_SIZE) { GridTile() } }
        for (row in newGrid.indices) {
            for (col in newGrid.indices) {
                newGrid[row][col].position = Pair(row, col)
            }
        }
        val randomRow = Random.nextInt(DIM_SIZE)
        val randomCol = Random.nextInt(DIM_SIZE)
        newGrid[randomRow][randomCol] = GridTile(value = 2, position = randomRow to randomCol)


        gameState.value = gameState.value.copy(
            gameGrid = newGrid,
            gameProgressStatus = GameProgressStatus.NOT_STARTED
        )
    }

}