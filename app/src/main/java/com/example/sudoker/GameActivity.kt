package com.example.sudoker

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.*
import android.widget.*
import java.lang.Exception
import java.util.*

class GameActivity : AppCompatActivity() {
    var solver = SudokuSolver()

    /* game state */
    private lateinit var solution: Array<IntArray>
    private var difficulty = 0
    private var timer: Timer? = null
    private fun generateGrid() {
        // generate a grid
//        solution = solver.getRandomGrid(NUMBER_OF_EMPTY_CELLS[difficulty])
        solution = solver.getRandomGrid(35)
        val masks = Array(9) { IntArray(9) }

        // compute masks
        for (row in 0..8) {
            for (col in 0..8) {
                if (solution[row][col] > 9) {
                    masks[row][col] = 0
                } else {
                    masks[row][col] = 1 shl solution[row][col]
                }
            }
        }
        grid = SudokuGrid(this, masks, solution)
    }

    private fun restoreGrid(solutionString: String, gridString: String) {
        // restore solution
        solution = Array(9) { IntArray(9) }
        var scanner = Scanner(solutionString)
        for (row in 0..8) {
            for (col in 0..8) {
                solution[row][col] = scanner.nextInt()
            }
        }
        // restore masks
        val masks = Array(9) { IntArray(9) }
        scanner = Scanner(gridString)
        for (row in 0..8) {
            for (col in 0..8) {
                masks[row][col] = scanner.nextInt()
            }
        }
        grid = SudokuGrid(this, masks, solution)
    }

//    private fun saveGame() {
//        if (status < -1) return
//        val currentMask: Array<IntArray> = grid.getCurrentMasks()
//        val state = GameState(status, difficulty, timer.getElapsedSeconds(), solution, currentMask)
//        try {
//            val DBHelper: DatabaseHelper = DatabaseHelper.newInstance(this)
//            val database: SQLiteDatabase = DBHelper.getWritableDatabase()
//            database.insert("GameState", null, state.getContentValues())
//        } catch (e: Exception) {
//            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
//        }
//    }

    override fun onPause() {
//        saveGame()
        super.onPause()
    }

    var wannaBack = false
    override fun onBackPressed() {
        if (wannaBack) {
            super.onBackPressed()
            return
        }
        wannaBack = true
        Toast.makeText(this, "Bấm 'BACK' lần nữa để quay lại menu chính", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ wannaBack = false }, 3000)
    }

//    @SuppressLint("ResourceType")
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_solve -> onClickSolve()
//            R.id.action_reset -> onClickReset()
//            R.id.action_autofill -> onClickAutoFill()
//            R.id.action_tutorial -> onClickTutorial()
//            else -> {
//            }
//        }
//        return true
//    }

//    private fun onClickAutoFill() {
//        if (status < -1) return
//        if (status == -1) {
//            autoFill()
//        } else {
//            val dialog: AlertDialog.Builder = Builder(this, R.style.Theme_AppCompat_Light_Dialog)
//            dialog.setMessage("Nếu sử dụng tính năng này, kết quả của bạn sẽ không được công nhận trên bảng xếp hạng. \nBạn có chắc chắn muốn sử dụng?")
//                .setTitle("Chú ý\n")
//                .setPositiveButton("Đồng ý",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        status = -1
//                        autoFill()
//                    })
//                .setNegativeButton("Từ chối",
//                    DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
//                .show()
//        }
//    }

//    private fun onClickTutorial() {
//        val dialog: AlertDialog.Builder = Builder(this, R.style.MyTutorialTheme)
//        val message: Spannable =
//            SpannableWithImage.getTextWithImages(this, getString(R.string.tutorial), 50)
//        val position = getString(R.string.tutorial).indexOf("Chú ý:")
//        message.setSpan(RelativeSizeSpan(1.2f),
//            position,
//            position + 6,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        dialog.setMessage(message).setTitle("Hướng dẫn").show()
//    }

//    private fun autoFill() {
//        grid!!.fill()
//        updateNumpad()
//    }

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
//        val bundle = intent.extras
        val bundle = Bundle()
//        difficulty = bundle!!.getInt("difficulty", 0)
//        status = bundle.getInt("status", 0)

        // lock screen orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // setup action bar title
//        supportActionBar!!.setTitle(DIFFICULT_NAME[difficulty])

        // hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val btnSubmit: Button = findViewById(R.id.btn_submit)
        btnSubmit.setTypeface(AppConstant.APP_FONT)
        val solutionString = bundle.getString("solutionString", "none")
        val gridString = bundle.getString("gridString", "none")
//        if (solutionString == "none" || gridString == "none") {
            generateGrid()
//        } else {
//            restoreGrid(solutionString, gridString)
//        }
        numpad = Numpad(this)
        val elapsedTime = bundle.getInt("elapsedSeconds", 0)
        timer = Timer(this, elapsedTime)
        timer!!.start()
    }
//
//    fun onClickSubmit(view: View?) {
//        if (!grid!!.isLegalGrid()) {
//            Toast.makeText(this, "Hãy hoàn thiện bảng", Toast.LENGTH_LONG).show()
//            return
//        }
//        if (solver.checkValidGrid(grid.getNumbers())) {
//            Toast.makeText(this, "Chúc mừng, đáp án chính xác", Toast.LENGTH_LONG).show()
//            status = 0
//            if (status >= 0) {
//                val dialog: AlertDialog.Builder =
//                    Builder(this, R.style.Theme_AppCompat_Light_Dialog)
//                dialog.setMessage("Bạn có muốn lưu kết quả trên bảng xếp hạng?")
//                    .setTitle("Thông báo\n")
//                    .setPositiveButton("Đồng ý",
//                        DialogInterface.OnClickListener { dialogInterface, i -> saveAchievement() })
//                    .setNegativeButton("Từ chối",
//                        DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
//                    .show()
//                timer.stop()
//                status = 1
//            }
//            // set status to GAME_DONE
//            status = -3
//        } else {
//            Toast.makeText(this, "Đáp án sai", Toast.LENGTH_LONG).show()
//        }
//    }

//    private fun saveAchievement() {
//        val intent = Intent(this, SaveAchievementActivity::class.java)
//        intent.putExtra("difficulty", difficulty)
//        intent.putExtra("elapsedSeconds", timer.getElapsedSeconds())
//        startActivity(intent)
//    }

//    fun onClickSolve() {
//        if (status >= 0) {
//            val dialog: AlertDialog.Builder = Builder(this, R.style.Theme_AppCompat_Light_Dialog)
//            dialog.setMessage("Nếu sử dụng tính năng này, kết quả của bạn sẽ không được công nhận trên bảng xếp hạng. \nBạn có chắc chắn muốn sử dụng?")
//                .setTitle("Chú ý\n")
//                .setPositiveButton("Đồng ý",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        status = -2
//                        onClickSolve()
//                    })
//                .setNegativeButton("Từ chối",
//                    DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
//                .show()
//        }
//        if (status < 0) {
//            status = -2
//            grid!!.showSolution()
//            updateNumpad()
//            timer.stop()
//        }
//    }

    private fun onClickReset() {
        if (status < -1) return
        grid.clear()
        updateNumpad()
    }

    companion object {
        val NUMBER_OF_EMPTY_CELLS = intArrayOf(0, 30, 35, 45, 50)
        val DIFFICULT_NAME = arrayOf("NONE", "Easy", "Normal", "Hard", "Extreme")
        @SuppressLint("StaticFieldLeak")
        private lateinit var grid: SudokuGrid
        @SuppressLint("StaticFieldLeak")
        private lateinit var numpad: Numpad
        private val stack = Stack<CellState>()
        private var status // -3 game done | -2: auto solved | -1: auto fill | 0: playing | 1: player solved
                = 0

        fun updateNumpad() {
            val selectedCell: Cell = grid.getSelectedCell()
            numpad.update(selectedCell.getMask(), selectedCell.isMarked())
        }

        @JvmStatic
        fun onPressNumpad(number: Int) {
            if (status < -1) return
            val selectedCell: Cell = grid.getSelectedCell()
            if (number < 10) {
                // backup current selected cell state
                stack.push(selectedCell.state)
                // add a number to selected cell
                selectedCell.addNumber(number)
            } else if (number == 10) {
                // mark selected cell
                selectedCell.setMarked(!selectedCell.isMarked())
            } else if (number == 11) {
                // restore previous selected cell state
                if (!stack.isEmpty()) {
                    val preState = stack.peek()
                    stack.pop()
                    val index = preState.index
                    val row = index / 9
                    val col = index - row * 9
                    grid.getCell(row, col).setMask(preState.mask)
                }
            }
            updateNumpad()
        }

        fun setNumpadVisible(state: Int) {
            numpad.setVisibility(state)
        }

        fun highlightNeighborCells(index: Int) {
            grid.highlightNeighborCells(index)
        }

        fun setSelectedCell(index: Int) {
            grid.setSelectedCell(index)
        }

    }
}
