package com.nuclearcode.teyesmusicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import com.nuclearcode.teyesmusicplayer.di.DaggerAudioPlaybackServiceComponent
import com.nuclearcode.teyesmusicplayer.di.ModuleCore
import javax.inject.Inject

class AudioPlaybackService : Service() {

    @Inject
    lateinit var playerManager: AudioPlayerManager

    private val component by lazy {
        DaggerAudioPlaybackServiceComponent.builder()
            .moduleCore(ModuleCore(this.application))
            .build()
    }
    private lateinit var media3Session: MediaSession
    private lateinit var mediaSessionCompat: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        component.injectService(this)

        // Media3 Session
        media3Session = MediaSession.Builder(this, playerManager.exoPlayer)
            .setId("MyAudioSession")
            .build()

        // MediaSessionCompat для уведомления
        mediaSessionCompat = MediaSessionCompat(this, "CompatSession")
        mediaSessionCompat.isActive = true

        // Уведомление
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val audioUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("AUDIO_URI", Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra("AUDIO_URI")
        }

        audioUri?.let {
            playerManager.exoPlayer.setMediaItem(MediaItem.fromUri(it))
            playerManager.exoPlayer.prepare()
            playerManager.exoPlayer.play()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        playerManager.exoPlayer.release()
        media3Session.release()
        mediaSessionCompat.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification {
        createNotificationChannel()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Аудио воспроизведение")
            .setContentText("Трек играет")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSessionCompat.sessionToken)
                    .setShowActionsInCompactView(0, 1)
            )
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Аудио плеер",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Плеер работает в фоне"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "audio_playback_channel"
        private const val NOTIFICATION_ID = 1001
    }
}
