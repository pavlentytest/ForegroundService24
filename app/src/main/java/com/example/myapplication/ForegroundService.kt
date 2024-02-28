package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ServiceCompat


class ForegroundService : Service() {

    private var mPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind")
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        startAsForegroundService()
        mPlayer?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        Toast.makeText(this, "Foreground Service created", Toast.LENGTH_SHORT).show()
        mPlayer = MediaPlayer.create(this, R.raw.bet)
        mPlayer!!.isLooping = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        mPlayer!!.stop()
        Toast.makeText(this, "Foreground Service destroyed", Toast.LENGTH_SHORT).show()
    }

    /**
     * Promotes the service to a foreground service, showing a notification to the user.
     *
     * This needs to be called within 10 seconds of starting the service or the system will throw an exception.
     */
    private fun startAsForegroundService() {
        // create the notification channel
        NotificationsHelper.createNotificationChannel(this)

        // promote service to foreground service
        ServiceCompat.startForeground(
            this,
            1,
            NotificationsHelper.buildNotification(this),
            0
        )
    }

    companion object {
        private const val TAG = "ExampleForegroundService"
    }
}
