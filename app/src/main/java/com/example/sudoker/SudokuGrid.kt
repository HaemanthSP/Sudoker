package com.example.sudoker

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.GridView
import android.widget.BaseAdapter
import android.view.ViewGroup
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SudokuGrid(private val mContext: Context, masks: Array<IntArray>, solution: Array<IntArray>) {
    private val mCells: Array<Array<Cell>> = Array(9) {Array(9){Cell(mContext)}}
    private val mGridView: GridView = (mContext as AppCompatActivity).findViewById(R.id.grid_sudoku)
    private val mBoxes = arrayOfNulls<Box>(9)
    private lateinit var mselectedCell: Cell
    private val mSolution = Array(9) { IntArray(9) }

    // What is a mask of a cell?
    val currentMasks: Array<IntArray>
        get() {
            val values = Array(9) { IntArray(9) }
            for (row in 0..8) {
                for (col in 0..8) {
                    values[row][col] = mCells[row][col].getMask()
                }
            }
            return values
        }

    val numbers: Array<IntArray>
        get() {
            val values = Array(9) { IntArray(9) }
            for (row in 0..8) {
                for (col in 0..8) {
                    values[row][col] = mCells[row][col].number
                }
            }
            return values
        }

    fun fill() {
        val rows = Array(9) { BooleanArray(10) }
        val cols = Array(9) { BooleanArray(10) }
        val boxes = Array(9) { BooleanArray(10) }
        for (row in 0..8) {
            for (col in 0..8) {
                if (mCells[row][col].isLocked) {
                    val box = (row / 3) * 3 + col / 3
                    val number = mCells[row][col].number
                    rows[row][number] = true
                    cols[col][number] = true
                    boxes[box][number] = true
                }
            }
        }
        for (row in 0..8) {
            for (col in 0..8) {
                if (!mCells[row][col].isLocked) {
                    var mask = 1022 // full numbers = 2^10 - 2.
                    val box = row / 3 * 3 + col / 3
                    for (x in 1..9) {
                        if (rows[row][x] || cols[col][x] || boxes[box][x])
                            mask = mask and (1 shl x).inv()
                    }
                    mCells[row][col].setMask(mask)
                }
            }
        }
    }

    fun getSelectedCell(): Cell {
        return mselectedCell
    }

    fun setSelectedCell(index: Int) {
        val row = index / 9
        val col = index - row * 9
        mselectedCell = mCells[row][col]
    }

    val isLegalGrid: Boolean
        get() {
            for (row in mCells) {
                for (cell in row) {
                    if (Integer.bitCount(cell.getMask()) > 1) {
                        return false
                    }
                }
            }
            return true
        }

    fun clear() {
        for (row in mCells) {
            for (cell in row) {
                if (!cell.isLocked) {
                    cell.addNumber(0)
                }
            }
        }
    }

    fun getCell(row: Int, col: Int): Cell {
        return mCells[row][col]
    }

    fun highlightNeighborCells(index: Int) {
        val row = index / 9
        val col = index - row * 9
        val box = (row / 3) * 3 + col / 3
        for (i in 0..8) {
            for (j in 0..8) {
                val k = (i / 3) * 3 + j / 3
                if (i == row || j == col || k == box) {
                    mCells[i][j].setHighLight()
                } else {
                    mCells[i][j].setNoHighLight()
                }
            }
        }
    }

    fun showSolution() {
        for (row in 0..8) {
            for (col in 0..8) {
                // get actually number
                val number = mSolution[row][col] and 1024.inv()
                val cell = mCells[row][col]
                cell.number = number

                // if cell is unlocked, color cell number to RED
                if (!cell.isLocked) {
                    cell.setTextColor(Color.RED)
                    cell.textSize = Cell.CELL_DEFAULT_TEXT_SIZE.toFloat()
                }
            }
        }
    }

    /* Grid adapter */
    inner class SudokuGridAdapter(var mBoxes: Array<Box?>) : BaseAdapter() {
        override fun getCount(): Int {
            return mBoxes.size
        }

        override fun getItem(i: Int): Any? {
            return mBoxes[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
            return mBoxes[i]
        }
    }

    /* Box*/
    inner class Box : GridView(mContext) {
        init {
            verticalSpacing = AppConstant.BOX_LINE_SPACING
            horizontalSpacing = AppConstant.BOX_LINE_SPACING
            numColumns = 3
            gravity = Gravity.CENTER
            setBackgroundResource(R.color.GRID_BACKGROUND_COLOR)
            layoutParams = LayoutParams(AUTO_FIT, AppConstant.BOX_HEIGHT)
        }
    }

    /* Box adapter */
    inner class BoxAdapter(private val mIndex: Int) : BaseAdapter() {
        override fun getCount(): Int {
            return 9
        }

        override fun getItem(i: Int): Any? {
            return null
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
            val row = mIndex / 3 * 3 + position / 3
            val col = mIndex % 3 * 3 + position % 3
            return mCells[row][col]
        }
    }

    init {
        for (row in 0..8) {
            for (col in 0..8) {
                val box = row / 3 * 3 + col / 3
                // copy solution
                mSolution[row][col] = solution[row][col]

                // initialize cell
                val defaultColor = if (box % 2 == 0) R.color.EVEN_BOX_COLOR else R.color.ODD_BOX_COLOR

                mCells[row][col] = Cell(mContext, row * 9 + col, defaultColor, solution[row][col] <= 9);
                mCells[row][col].setMask(masks[row][col])
            }
        }

        // initialize grid
        for (i in 0..8) {
            mBoxes[i] = Box()
            val boxAdapter = BoxAdapter(i)
            mBoxes[i]!!.adapter = boxAdapter
        }
        val gridAdapter = SudokuGridAdapter(mBoxes)
        mGridView.adapter = gridAdapter
        Log.d("SudokuGrid", "Count: " + gridAdapter.count)
    }
}