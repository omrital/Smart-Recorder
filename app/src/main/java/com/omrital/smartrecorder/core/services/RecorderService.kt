package com.omrital.smartrecorder.core.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

class RecorderService: Service() {

    private lateinit var timer: Timer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("omri", "onStartCommand called")
        Log.d("omri", "onStartCommand:  " + (if(intent==null) "intent null" else "intent not null"))

        timer = Timer()
        class Task : TimerTask() {
            override fun run() {
                Log.d("omri", "invoke timer task")
            }
        }
        timer.schedule(Task(), 1000, 5000)
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("omri", "onTaskRemoved called, user killed the app")

        super.onTaskRemoved(rootIntent)
    }
}