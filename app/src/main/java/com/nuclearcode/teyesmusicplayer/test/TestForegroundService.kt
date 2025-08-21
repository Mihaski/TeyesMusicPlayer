package com.nuclearcode.teyesmusicplayer.test

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nuclearcode.teyesmusicplayer.R

class TestForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "test_channel"
        const val NOTIFICATION_ID = 100
    }

    override fun onCreate() {
        super.onCreate()
        createActiveAudioNotificationChannel(this)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Test")
            .setContentText("Service is running")
            .setSmallIcon(R.drawable.ic_music_note) // убедись, что иконка есть
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        openNotificationSettings(this, "102")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }
    fun openNotificationSettings(context: Context, channelId: String) {
        val intent = Intent().apply {
            action = android.provider.Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
            putExtra(android.provider.Settings.EXTRA_CHANNEL_ID, channelId)
        }
        context.startActivity(intent)
    }

    private fun createActiveAudioNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "active_audio_channel"
            val channelName = "Active Audio Playback"

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Audio playback notifications"
                // Категория: медиа/аудио
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.setSound(null, null) // без звука
                    this.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    this.importance = NotificationManager.IMPORTANCE_LOW
                    this.group = null
                    this.enableVibration(false)
                    this.setShowBadge(false)
                }
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            // На API 33+ можно принудительно включить канал через PendingIntent для теста
            // Но чаще достаточно создать новый уникальный channelId
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
