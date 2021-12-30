package com.example.sudoker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import android.view.MotionEvent
import android.view.Gravity
import java.util.HashMap

/**
 * Created by tuana on 14-03-2018.
 */
@SuppressLint("ResourceAsColor")
class Cell(
    context: Context?,
    val index: Int,
    val defaultColor: Int
//    val highlightColor: Int,
) : AppCompatTextView(
    context!!) {
    private val markedColor: Int
    var isLocked = false
    private var isMarked = false
    private var mask = 0
    val state: CellState
        get() {
            val state = CellState()
            state.mask = mask
            state.index = index
            return state
        }

    fun isMarked(): Boolean {
        return isMarked
    }

    fun setMarked(state: Boolean) {
        isMarked = state
        setBackgroundResource(if (isMarked) markedColor else R.color.TARGET_CELL_COLOR)
    }

    fun addNumber(number: Int) {
        setMask(mask xor (1 shl number))
    }

    fun getMask(): Int {
        return mask
    }

    var number: Int
        get() = maskToNumber[mask]!!
        set(number) {
            val mask = 1 shl number
            setMask(mask)
        }

    fun setHighLight() {
        val color = if (isMarked) MARKED_CELL_COLOR else highlightColor
        setBackgroundResource(color)
    }

    fun setNoHighLight() {
        val color = if (isMarked) MARKED_CELL_COLOR else defaultColor
        setBackgroundResource(color)
    }

    fun setMask(mask: Int) {
        this.mask = mask
        if (mask != 0 && mask % 2 == 0) {
            val counter = Integer.bitCount(mask)
            if (counter > 1) {
                val format = "1  2  3\n4  5  6\n7  8  9".toCharArray()
                for (x in 1..9) {
                    if ((mask shr x) % 2 == 0) {
                        format[indexOfNumber[x]] = ' '
                    }
                }
                textSize = (CELL_DEFAULT_TEXT_SIZE / 2).toFloat()
                text = String(format)
            } else {
                textSize = CELL_DEFAULT_TEXT_SIZE.toFloat()
                text = maskToNumber[mask].toString()
            }
        } else {
            this.mask = 0
            text = ""
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        GameActivity.highlightNeighborCells(index);
//        if (isLocked) {
//            GameActivity.setNumpadVisible(View.INVISIBLE);
//        } else {
        setBackgroundResource(if (isMarked) R.color.MARKED_CELL_COLOR else R.color.TARGET_CELL_COLOR)
        //            GameActivity.setNumpadVisible(View.VISIBLE);
//        }
//        GameActivity.setSelectedCell(index);
//        GameActivity.updateNumpad();
        return true
    }

    companion object {
        @JvmField
        var CELL_DEFAULT_TEXT_SIZE = 18
        @JvmField
        var CELL_HEIGHT = 0
        private val indexOfNumber =
            intArrayOf(0, 0, 3, 6, 8, 11, 14, 16, 19, 22) // in format cell string
        const val MARKED_CELL_COLOR = R.color.MARKED_CELL_COLOR
        val maskToNumber: HashMap<Int?, Int?> = object : HashMap<Int?, Int?>() {
            init {
                put(0, 0)
                put(1, 0)
                put(2, 1)
                put(4, 2)
                put(8, 3)
                put(16, 4)
                put(32, 5)
                put(64, 6)
                put(128, 7)
                put(256, 8)
                put(512, 9)
            }
        }
    }

    init {
        markedColor = R.color.MARKED_CELL_COLOR
        height = CELL_HEIGHT
        gravity = Gravity.CENTER
        typeface = AppConstant.APP_FONT
        setBackgroundResource(defaultColor)
        if (highlightColor == R.color.HIGHLIGHT_LOCKED_CELL_COLOR) {
            isLocked = true
            setTextColor(Color.BLACK)
        } else {
            isLocked = false
            setTextColor(Color.BLUE)
        }
    }
}