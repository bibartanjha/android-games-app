package com.example.android_games_app.games.twentyfortyeight.utils

import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.DIM_SIZE

object GridFunctions {
    fun shiftGridUp(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        var tempGrid = rotateCounterClockwise(grid)
        tempGrid = moveTilesLeftAndMerge(tempGrid)
        return rotateClockwise(tempGrid)
    }

    fun shiftGridDown(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        var tempGrid = rotateClockwise(grid)
        tempGrid = moveTilesLeftAndMerge(tempGrid)
        return rotateCounterClockwise(tempGrid)
    }

    fun shiftGridRight(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        var tempGrid = rotateClockwise(grid)
        tempGrid = rotateClockwise(tempGrid)
        tempGrid = moveTilesLeftAndMerge(tempGrid)
        tempGrid = rotateClockwise(tempGrid)
        return rotateClockwise(tempGrid)
    }

    fun shiftGridLeft(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        return moveTilesLeftAndMerge(grid)
    }

    fun rotateClockwise(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        val transposedGrid = transpose(grid)
        return reverseRows(transposedGrid)
    }

    fun rotateCounterClockwise(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        val reversedGrid = reverseRows(grid)
        return transpose(reversedGrid)
    }

    fun transpose(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        val transposedGrid = List(grid.size) { MutableList(grid.size) { 0 } }
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
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        return grid.map {
            it.toMutableList().apply {
                reverse()
            }}
    }

    fun moveTilesLeftAndMerge(
        grid: List<MutableList<Int>>
    ): List<MutableList<Int>> {
        return grid.map { row ->
            val newRow = MutableList(DIM_SIZE) { 0 }
            var currIndexInNewRow = 0
            for (col in row.indices) {
                val tileValue = row[col]
                if (tileValue != 0) {
                    newRow[currIndexInNewRow] = tileValue
                    currIndexInNewRow += 1
                }
            }

            var i = 0
            while (i < newRow.size - 1) {
                if (newRow[i] == newRow[i + 1]) {
                    newRow[i] *= 2
                    newRow[i + 1] = 0
                    i += 1
                }
                i += 1
            }

            val newRow2 = MutableList(DIM_SIZE) { 0 }
            var currIndexInNewRow2 = 0
            for (col in newRow.indices) {
                val tileValue = newRow[col]
                if (tileValue != 0) {
                    newRow2[currIndexInNewRow2] = tileValue
                    currIndexInNewRow2 += 1
                }
            }

            newRow2
        }
    }

    fun containsAdjacentTilesWithEqualValue(
        grid: List<MutableList<Int>>
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
        grid: List<MutableList<Int>>,
        valueToSearchFor: Int
    ): MutableList<Pair<Int, Int>> {
        val indicesWithValue = mutableListOf<Pair<Int, Int>>()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == valueToSearchFor) {
                    indicesWithValue.add(Pair(row, col))
                }
            }
        }
        return indicesWithValue
    }
}