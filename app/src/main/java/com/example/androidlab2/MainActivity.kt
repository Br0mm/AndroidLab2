package com.example.androidlab2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    lateinit var sharedPref: SharedPreferences

    companion object {
        val SECONDS = "seconds"
    }

    var backgroundThread = Thread {
        while (true) {
            try {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
                }
            } catch (e: InterruptedException) {
                return@Thread
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
    }

    override fun onStart() {
        super.onStart()
        sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        secondsElapsed = sharedPref.getInt(SECONDS, 0)
        backgroundThread.start()
    }

    override fun onPause() {
        super.onPause()
        sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(SECONDS, secondsElapsed)
            apply()
        }
        backgroundThread.interrupt()
    }

}