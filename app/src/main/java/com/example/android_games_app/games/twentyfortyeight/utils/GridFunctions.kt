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
                val tileValue = newRow[col].value
                if (tileValue != 0) {
                    newRow2[currIndexInNewRow2].value = tileValue
                    newRow2[currIndexInNewRow2].hadRecentMerge = newRow[col].hadRecentMerge
                    currIndexInNewRow2 += 1
                }
            }

            newRow2
        }
    }
}