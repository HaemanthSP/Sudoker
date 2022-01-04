package com.example.sudoker

import java.util.*

/**
 * Created by tuana on 10-03-2018.
 */
class SudokuSolver {
    private val values = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val rnd = Random()
    private val grid: Array<IntArray>
    private val rows: Array<BooleanArray>
    private val cols: Array<BooleanArray>
    private val boxes: Array<BooleanArray>
    private var genFlag = false
    private fun clearGrid() {
        for (r in 0..8) {
            for (c in 0..8) {
                grid[r][c] = 0
            }
        }
        for (i in 0..8) {
            for (v in 1..9) {
                rows[i][v] = false
                cols[i][v] = false
                boxes[i][v] = false
            }
        }
    }

    private fun randomValuesArray(numberOfTurns: Int) {
        for (turn in 0 until numberOfTurns) {
            val randomIndex = 1 + rnd.nextInt(8)
            val tmp = values[0]
            values[0] = values[randomIndex]
            values[randomIndex] = tmp
        }
    }

    private fun generate(pos: Int) {
        if (pos == 81) {
            genFlag = false
        } else if (genFlag) {
            val row = pos / 9
            val col = pos - row * 9
            val box = row / 3 * 3 + col / 3
            randomValuesArray(5)
            for (`val` in values) {
                if (genFlag && rows[row][`val`] == false && cols[col][`val`] == false && boxes[box][`val`] == false) {
                    grid[row][col] = `val`
                    rows[row][`val`] = true
                    cols[col][`val`] = true
                    boxes[box][`val`] = true
                    generate(pos + 1)
                    rows[row][`val`] = false
                    cols[col][`val`] = false
                    boxes[box][`val`] = false
                }
            }
        }
    }

    fun getRandomGrid(numberOfEmptyCells: Int): Array<IntArray> {
        clearGrid()
        genFlag = true
        generate(0)
        /* erase cells */
        var i = 0
        while (i < numberOfEmptyCells) {
            val row = rnd.nextInt(9)
            val col = rnd.nextInt(9)
            if (grid[row][col] <= 9) {
                /* the 10-th bit mark the cell is empty*/
                grid[row][col] = grid[row][col] or 1024
                ++i
            }
        }
        return grid
    }

    fun checkValidGrid(grid: Array<IntArray>): Boolean {
        val rowSet = Array(9) { BooleanArray(10) }
        val colSet = Array(9) { BooleanArray(10) }
        val boxSet = Array(9) { BooleanArray(10) }
        for (row in 0..8) {
            for (col in 0..8) {
                val value = grid[row][col]
                if (value == 0) return false
                val box = row / 3 * 3 + col / 3
                if (rowSet[row][value] || colSet[col][value] || boxSet[box][value]) return false
                rowSet[row][value] = true
                colSet[col][value] = true
                boxSet[box][value] = true
            }
        }
        return true
    }

    init {
        grid = Array(9) { IntArray(9) }
        rows = Array(9) { BooleanArray(10) }
        cols = Array(9) { BooleanArray(10) }
        boxes = Array(9) { BooleanArray(10) }
        randomValuesArray(10)
    }
}