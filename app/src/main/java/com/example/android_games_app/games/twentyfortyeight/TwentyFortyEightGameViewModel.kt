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

        val tilesWith2048 = mutableListOf<Pair<Int, Int>>()
        val tilesWithZero = mutableListOf<Pair<Int, Int>>()
        var containsAdjacentTilesWithEqualValue = false

        for (row in updatedGrid.indices) {
            for (col in updatedGrid[row].indices) {
                updatedGrid[row][col].isNewTile = false

                if (updatedGrid[row][col].value == 2048) {
                    tilesWith2048.add(Pair(row, col))
                } else if (updatedGrid[row][col].value == 0) {
                    tilesWithZero.add(Pair(row, col))
                }

                if (!containsAdjacentTilesWithEqualValue) { // note to self: adding this check because if it is already true, then no need to check any more
                    if ((row + 1) < updatedGrid.size) {
                        if (updatedGrid[row][col] == updatedGrid[row + 1][col]) {
                            containsAdjacentTilesWithEqualValue = true
                        }
                    }
                    if ((col + 1) < updatedGrid[row].size) {
                        if (updatedGrid[row][col] == updatedGrid[row][col + 1]) {
                            containsAdjacentTilesWithEqualValue = true
                        }
                    }
                }
            }
        }

        if (tilesWith2048.isNotEmpty()) {
            updatedGameStatus = GameProgressStatus.WON
        } else {
            if (tilesWithZero.isEmpty() && (!containsAdjacentTilesWithEqualValue)) {
                updatedGameStatus = GameProgressStatus.LOST
            } else {
                val randomTile = tilesWithZero.random()
                updatedGrid[randomTile.first][randomTile.second] = GridTile(
                    value = 2,
                    isNewTile = true
                )
            }
        }

        gameState.value = gameStateValue.copy(
            gameGrid = updatedGrid,
            gameProgressStatus = updatedGameStatus
        )
    }

    fun startNewGame() {
        val newGrid = List(DIM_SIZE) { MutableList(DIM_SIZE) { GridTile() } }
        val randomRow = Random.nextInt(DIM_SIZE)
        val randomCol = Random.nextInt(DIM_SIZE)
        newGrid[randomRow][randomCol] = GridTile(value = 2)


        gameState.value = gameState.value.copy(
            gameGrid = newGrid,
            gameProgressStatus = GameProgressStatus.NOT_STARTED
        )
    }

    fun resetHadRecentMerge(row: Int, col: Int) {
        val updatedGrid = gameState.value.gameGrid
        updatedGrid[row][col].hadRecentMerge = false
        gameState.value = gameState.value.copy(
            gameGrid = updatedGrid
        )
    }

    fun resetIsNewTile(row: Int, col: Int) {
        val updatedGrid = gameState.value.gameGrid
        updatedGrid[row][col].isNewTile = false
        gameState.value = gameState.value.copy(
            gameGrid = updatedGrid
        )
    }

}