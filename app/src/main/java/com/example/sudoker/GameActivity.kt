package com.example.sudoker

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class GameActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityGameBinding
    private lateinit var grid: SudokuGrid
    private lateinit var gridView: GridView
    private lateinit var solution: Array<IntArray>

    val solver = SudokuSolver()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_game)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var bundle = intent.extras
        var solutionString = bundle?.getString("solutionString", "none")
        var gridString = bundle?.getString("gridString", "none")

//        if (solutionString.equals("none") || gridString.equals("none"))
//        {
            generateGrid();
//        } else {
            // Implement resume game
//        }

        // Numpad
        // Timer
    }

    public fun generateGrid(){
        // generate a grid
//        solution = solver.getRandomGrid(GameActivity.NUMBER_OF_EMPTY_CELLS.get(difficulty))
        solution = solver.getRandomGrid(35)
        val masks = Array(9) { IntArray(9) }

        // compute masks

        // compute masks
        for (row in 0..8) {
            for (col in 0..8) {
                if (solution.get(row).get(col) > 9) {
                    masks[row][col] = 0
                } else {
                    masks[row][col] = 1 shl solution.get(row).get(col)
                }
            }
        }
        Log.d("Game", "Generated Solution " + solution)
        grid = SudokuGrid(this, masks, solution)
        gridView = findViewById(R.id.grid_sudoku)
    }

}