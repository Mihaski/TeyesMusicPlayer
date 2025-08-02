package com.nuclearcode.teyesmusicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.nuclearcode.teyesmusicplayer.di.DaggerAudioPlaybackServiceComponent
import com.nuclearcode.teyesmusicplayer.di.ModuleCore
import javax.inject.Inject

class AudioPlaybackService : Service() {

    @Inject
    lateinit var playerManager: AudioPlayerManager
    private lateinit var playlist: List<AudioFile>
    private lateinit var mediaSessionCompat: MediaSessionCompat

    private val component by lazy {
        DaggerAudioPlaybackServiceComponent.builder()
            .moduleCore(ModuleCore(this.application))
            .build()
    }

    private var currentIndex = 0

    override fun onCreate() {
        super.onCreate()
        component.injectService(this)
        mediaSessionCompat = MediaSessionCompat(this, "CompatSession")

        // MediaSession и уведомление
        setupMediaSession()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            PlaybackCommand.PLAY.name -> {
                playlist = parseIntentApiLevel(intent) ?: emptyList()
                currentIndex = intent.getIntExtra("START_INDEX", 0)
                playCurrent()
            }
            PlaybackCommand.PAUSE.name -> playerManager.pause()
            PlaybackCommand.STOP.name -> stopSelf()
            PlaybackCommand.NEXT.name -> {
                if (currentIndex + 1 < playlist.size) {
                    currentIndex++
                    playCurrent()
                }
            }
            PlaybackCommand.PREVIOUS.name -> {
                if (currentIndex - 1 >= 0) {
                    currentIndex--
                    playCurrent()
                }
            }
        }
        return START_STICKY
    }

    private fun playCurrent() {
        val track = playlist.getOrNull(currentIndex) ?: return
        playerManager.play(track)
        // обновить уведомление при необходимости
    }

    private fun setupMediaSession() {
        // MediaSessionCompat, уведомление и т.д.
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        playerManager.release()
        super.onDestroy()
    }

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

    fun parseIntentApiLevel(intent: Intent?) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableArrayListExtra("PLAYLIST", AudioFile::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableArrayListExtra("PLAYLIST")
        }

    companion object {
        private const val CHANNEL_ID = "audio_playback_channel"
        private const val NOTIFICATION_ID = 1
    }
}