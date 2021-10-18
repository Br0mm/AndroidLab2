package com.example.androidlab2

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    var threadStopped = false

    companion object {
        val SECONDS = "seconds"
        val STOP = "stop"
    }

    var backgroundThread = Thread {
        while (!threadStopped) {
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(SECONDS, secondsElapsed)
            putBoolean(STOP, false)
        }
        super.onSaveInstanceState(outState)
        threadStopped = true
        //backgroundThread.interrupt()
        Log.i("Save", "State saved")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                secondsElapsed = getInt(SECONDS)
                threadStopped = getBoolean(STOP)
                backgroundThread.start()
            }
        }
    }

}