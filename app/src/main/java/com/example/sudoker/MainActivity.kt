package com.example.sudoker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnPlay: Button
    private lateinit var btnResume: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide the status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        btnPlay = findViewById(R.id.btnPlay)
        btnResume = findViewById(R.id.btnResume)
    }

    public fun onClickPlay(view: View) {
        Log.d("MainActivity", "Play button Clicked")
       var intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}