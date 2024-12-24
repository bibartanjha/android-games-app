package com.example.android_games_app.games.twentyfortyeight.utils

import android.util.Log
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE

object GridFunctions {
    fun shiftGridUp(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        var tempGrid = rotateCounterClockwise(grid)
        tempGrid = moveTilesLeftAndMerge(tempGrid)
        return rotateClockwise(tempGrid)
    }

    fun shiftGridDown(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        var tempGrid = rotateClockwise(grid)
        tempGrid = moveTilesLeftAndMerge(tempGrid)
        return rotateCounterClockwise(tempGrid)
    }

    fun shiftGridRight(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        var tempGrid = rotateClockwise(grid)
        tempGrid = rotateClockwise(tempGrid)
        tempGrid = moveTilesLeftAndMerge(tempGrid)
        tempGrid = rotateClockwise(tempGrid)
        return rotateClockwise(tempGrid)
    }

    fun shiftGridLeft(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        return moveTilesLeftAndMerge(grid)
    }

    fun rotateClockwise(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        val transposedGrid = transpose(grid)
        return reverseRows(transposedGrid)
    }

    fun rotateCounterClockwise(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        val reversedGrid = reverseRows(grid)
        return transpose(reversedGrid)
    }

    fun transpose(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        val transposedGrid = List(grid.size) { MutableList(grid.size) { GridTile() } }
        for (row in 0..<DIM_SIZE) {
            transposedGrid[row][row] = grid[row][row]
            for (col in (row + 1)..<DIM_SIZE) {
                transposedGrid[row][col] = grid[col][row]
                transposedGrid[col][row] = grid[row][col]
            }
        }
        return transposedGrid
    }

    fun reverseRows(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        return grid.map {
            it.toMutableList().apply {
                reverse()
            }}
    }

    fun moveTilesLeftAndMerge(
        grid: List<MutableList<GridTile>>
    ): List<MutableList<GridTile>> {
        return grid.map { row ->
            val newRow = MutableList(DIM_SIZE) { GridTile() }
            var currIndexInNewRow = 0
            for (col in row.indices) {
                newRow[col].position = row[col].position

                val tileValue = row[col].value
                if (tileValue != 0) {
                    newRow[currIndexInNewRow].value = tileValue
                    currIndexInNewRow += 1
                }
            }

            var i = 0
            while (i < newRow.size - 1) {
                if (newRow[i].value > 0 && newRow[i].value == newRow[i + 1].value) {
                    newRow[i].value *= 2
                    newRow[i + 1].value = 0

                    newRow[i].hadRecentMerge = true
                    newRow[i + 1].hadRecentMerge = false

                    i += 1
                } else {
                    newRow[i].hadRecentMerge = false
                }
                i += 1
            }

            newRow[newRow.size - 1].hadRecentMerge = false

            val newRow2 = MutableList(DIM_SIZE) { GridTile() }
            var currIndexInNewRow2 = 0
            for (col in newRow.indices) {
                newRow2[col].hadRecentMerge = newRow[col].hadRecentMerge
                newRow2[col].position = newRow[col].position

                val tileValue = newRow[col].value
                if (tileValue != 0) {
                    newRow2[currIndexInNewRow2].value = tileValue
                    currIndexInNewRow2 += 1
                }
            }

            newRow2
        }
    }

    fun containsAdjacentTilesWithEqualValue(
        grid: List<MutableList<GridTile>>
    ): Boolean {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if ((row + 1) < grid.size) {
                    if (grid[row][col] == grid[row + 1][col]) {
                        return true
                    }
                }
                if ((col + 1) < grid[row].size) {
                    if (grid[row][col] == grid[row][col + 1]) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getAllTilesWithValue(
        grid: List<MutableList<GridTile>>,
        valueToSearchFor: Int
    ): MutableList<Pair<Int, Int>> {
        val indicesWithValue = mutableListOf<Pair<Int, Int>>()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col].value == valueToSearchFor) {
                    indicesWithValue.add(Pair(row, col))
                }
            }
        }
        return indicesWithValue
    }
}