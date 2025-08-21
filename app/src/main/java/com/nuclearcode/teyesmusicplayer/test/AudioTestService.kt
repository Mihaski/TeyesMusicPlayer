package com.nuclearcode.teyesmusicplayer.test

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media.session.MediaButtonReceiver
import com.nuclearcode.teyesmusicplayer.R

class AudioTestService : Service() {

    companion object {
        const val CHANNEL_ID = "audio_test_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREV = "action_prev"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_PLAY -> { /* обработка Play */ }
            ACTION_PAUSE -> { /* обработка Pause */ }
            ACTION_NEXT -> { /* обработка Next */ }
            ACTION_PREV -> { /* обработка Prev */ }
        }

        return START_STICKY
    }

    private fun buildNotification(): Notification {
        val playIntent = PendingIntent.getService(
            this, 0,
            Intent(this, AudioTestService::class.java).setAction(ACTION_PLAY),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val pauseIntent = PendingIntent.getService(
            this, 0,
            Intent(this, AudioTestService::class.java).setAction(ACTION_PAUSE),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val nextIntent = PendingIntent.getService(
            this, 0,
            Intent(this, AudioTestService::class.java).setAction(ACTION_NEXT),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val prevIntent = PendingIntent.getService(
            this, 0,
            Intent(this, AudioTestService::class.java).setAction(ACTION_PREV),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Audio Test")
            .setContentText("Playing music...")
            .setSmallIcon(android.R.drawable.ic_media_play) // убедись, что иконка есть
            .setLargeIcon(BitmapFactory.decodeResource(resources, android.R.drawable.ic_media_play))
            .setOnlyAlertOnce(true)
            .addAction(android.R.drawable.ic_media_previous, "Prev", prevIntent)
            .addAction(android.R.drawable.ic_media_play, "Play", playIntent)
            .addAction(android.R.drawable.ic_media_pause, "Pause", pauseIntent)
            .addAction(android.R.drawable.ic_media_next, "Next", nextIntent)
            .setStyle(MediaStyle().setShowActionsInCompactView(0,1,2))
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Audio Test Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Background audio playback" }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
