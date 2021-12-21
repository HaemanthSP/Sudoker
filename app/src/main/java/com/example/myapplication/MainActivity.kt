package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import android.widget.GridView


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        val rows = 3
        val columns = 3

        val grid: GridView = findViewById(R.id.grid)
        grid.numColumns = columns

        val matrixList: MutableList<Matrix> = ArrayList()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                matrixList.add(Matrix(i, j, data = (0..9).random()))
            }
        }

        val blockList: MutableList<Block> = ArrayList()
        for (x in 0 until rows) {
            for (y in 0 until columns) {
                blockList.add(Block(x, y, data=matrixList))
            }
        }

        val adapter = BlockAdapter(applicationContext, blockList)
        grid.adapter = adapter
    }
}

class Matrix(var i: Int, var j: Int, var data: Int)
class Block(var i: Int, var j: Int, var data: MutableList<Matrix>)