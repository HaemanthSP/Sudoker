package com.example.sudoker

import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.util.DisplayMetrics
import com.example.sudoker.AppConstant
import com.example.sudoker.R

/**
 * Created by tuana on 19-03-2018.
 */
object AppConstant {
    lateinit var APP_FONT: Typeface
    var SCREEN_SIZE: Point? = null
//    var APP_FONT: Typeface? = null
    var BOX_LINE_SPACING = 4
    var BOX_HEIGHT = 0
    fun init(context: Context) {
        val metrics = context.resources.displayMetrics
        SCREEN_SIZE = Point(metrics.widthPixels, metrics.heightPixels)
        Cell.CELL_HEIGHT = (SCREEN_SIZE!!.x - 120) / 9
        BOX_HEIGHT = Cell.CELL_HEIGHT * 3 + 2 * BOX_LINE_SPACING
        APP_FONT = Typeface.createFromAsset(context.assets, context.getString(R.string.app_font))
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}