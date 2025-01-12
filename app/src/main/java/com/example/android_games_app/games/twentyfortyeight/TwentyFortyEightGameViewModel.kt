package com.example.android_games_app.games.twentyfortyeight

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

    private val twentyFortyEightGameState = MutableStateFlow(TwentyFortyEightGameState())
    val getTwentyFortyEightGameState: StateFlow<TwentyFortyEightGameState> = twentyFortyEightGameState

    fun userMove(direction: SwipeDirection) {
        val gameStateValue = twentyFortyEightGameState.value
        if (gameStateValue.gameProgressStatus != GameProgressStatus.IN_PROGRESS) {
            return
        }

        val updatedGrid = when (direction) {
            SwipeDirection.LEFT -> GridFunctions.shiftGridLeft(gameStateValue.gameGrid)
            SwipeDirection.RIGHT -> GridFunctions.shiftGridRight(gameStateValue.gameGrid)
            SwipeDirection.UP -> GridFunctions.shiftGridUp(gameStateValue.gameGrid)
            SwipeDirection.DOWN -> GridFunctions.shiftGridDown(gameStateValue.gameGrid)
            SwipeDirection.NONE -> gameStateValue.gameGrid
        }

        var updatedGameStatus = gameStateValue.gameProgressStatus

        val tilesWith2048 = mutableListOf<Pair<Int, Int>>()
        val tilesWithZero = mutableListOf<Pair<Int, Int>>()
        var containsAdjacentTilesWithEqualValue = false
        var gridIsSame = true

        for (row in updatedGrid.indices) {
            for (col in updatedGrid[row].indices) {
                if (updatedGrid[row][col].value != gameStateValue.gameGrid[row][col].value) {
                    gridIsSame = false
                }
                updatedGrid[row][col].isNewTile = false

                if (updatedGrid[row][col].value == 2048) {
                    tilesWith2048.add(Pair(row, col))
                } else if (updatedGrid[row][col].value == 0) {
                    tilesWithZero.add(Pair(row, col))
                }

                if (!containsAdjacentTilesWithEqualValue) { // note to self: adding this check because if it is already true, then no need to check any more
                    if ((row + 1) < updatedGrid.size) {
                        if (updatedGrid[row][col].value == updatedGrid[row + 1][col].value) {
                            containsAdjacentTilesWithEqualValue = true
                        }
                    }
                    if ((col + 1) < updatedGrid[row].size) {
                        if (updatedGrid[row][col].value == updatedGrid[row][col + 1].value) {
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
            } else if ((!gridIsSame) && (tilesWithZero.isNotEmpty())) {
                val randomTile = tilesWithZero.random()
                val row = randomTile.first
                val col = randomTile.second
                val newValue = 2
                updatedGrid[row][col] = GridTile(
                    value = newValue,
                    isNewTile = true
                )

                if (!containsAdjacentTilesWithEqualValue) { // if it already contains two adjacent tiles, then we don't need to do this check
                    if (tilesWithZero.size == 1) { // this means that the new tile took up the last spot
                        var newTileHasAdjacentEqualTile = false

                        if ((row > 0) && (updatedGrid[row - 1][col].value == newValue)) {
                            newTileHasAdjacentEqualTile = true
                        }
                        else if ((row < (updatedGrid.size - 1)) && (updatedGrid[row + 1][col].value == newValue)) {
                            newTileHasAdjacentEqualTile = true
                        }
                        else if ((col > 0) && (updatedGrid[row][col - 1].value == newValue)) {
                            newTileHasAdjacentEqualTile = true
                        }
                        if ((col < (updatedGrid[row].size - 1)) && (updatedGrid[row][col + 1].value == newValue)) {
                            newTileHasAdjacentEqualTile = true
                        }

                        if (!newTileHasAdjacentEqualTile) {
                            updatedGameStatus = GameProgressStatus.LOST
                        }
                    }
                }
            }
        }

        twentyFortyEightGameState.value = gameStateValue.copy(
            gameGrid = updatedGrid,
            gameProgressStatus = updatedGameStatus
        )
    }

    fun startNewGame() {
        val newGrid = List(DIM_SIZE) { MutableList(DIM_SIZE) { GridTile() } }
        val randomRow = Random.nextInt(DIM_SIZE)
        val randomCol = Random.nextInt(DIM_SIZE)
        newGrid[randomRow][randomCol] = GridTile(value = 2)


        twentyFortyEightGameState.value = twentyFortyEightGameState.value.copy(
            gameGrid = newGrid,
            gameProgressStatus = GameProgressStatus.IN_PROGRESS
        )
    }

    fun resumeGame() {
        twentyFortyEightGameState.value = twentyFortyEightGameState.value.copy(
            gameProgressStatus = GameProgressStatus.IN_PROGRESS
        )
    }

    fun restartPressed() {
        twentyFortyEightGameState.value = twentyFortyEightGameState.value.copy(
            gameProgressStatus = GameProgressStatus.SHOWING_RESTART_SCREEN
        )
    }

    fun resetHadRecentMerge(row: Int, col: Int) {
        val updatedGrid = twentyFortyEightGameState.value.gameGrid
        updatedGrid[row][col].hadRecentMerge = false
        twentyFortyEightGameState.value = twentyFortyEightGameState.value.copy(
            gameGrid = updatedGrid
        )
    }

    fun resetIsNewTile(row: Int, col: Int) {
        val updatedGrid = twentyFortyEightGameState.value.gameGrid
        updatedGrid[row][col].isNewTile = false
        twentyFortyEightGameState.value = twentyFortyEightGameState.value.copy(
            gameGrid = updatedGrid
        )
    }

}