package com.nuclearcode.teyesmusicplayer.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.nuclearcode.teyesmusicplayer.PlayerApp
import com.nuclearcode.teyesmusicplayer.R
import javax.inject.Inject

class AudioPlaybackService : Service() {
    @Inject
    lateinit var playerManager: AudioPlayerManager
    private var playlist: List<AudioFile> = emptyList()
    private var currentIndex = 0
    private lateinit var mediaSessionCompat: MediaSessionCompat

    inner class AudioServiceBinder : Binder() {
        fun handleCommand(command: PlaybackCommand) {
            when (command) {
                is PlaybackCommand.Play -> {
                    playlist = command.playlist
                    playTrackAt(command.index)
                }
                is PlaybackCommand.Pause -> playerManager.pause()
                is PlaybackCommand.Resume -> playerManager.resume()
                is PlaybackCommand.Stop -> playerManager.stop()
                is PlaybackCommand.SeekTo -> playerManager.exoPlayer.seekTo(command.position)
                is PlaybackCommand.PlayNext -> playNext()
                is PlaybackCommand.PlayPrevious -> playPrevious()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = AudioServiceBinder()

    override fun onCreate() {
        super.onCreate()
        (application as PlayerApp).appComponent.injectService(this)

        mediaSessionCompat = MediaSessionCompat(this, "CompatSession")
        mediaSessionCompat.isActive = true
        playerManager.nextCallback = {
            playNext()
        }

        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    private fun playTrackAt(index: Int) {
        if (index in playlist.indices) {
            currentIndex = index
            playerManager.play(playlist[currentIndex])
        }
    }

    private fun playNext() {
        if (currentIndex + 1 < playlist.size) {
            playTrackAt(currentIndex + 1)
        }
    }

    private fun playPrevious() {
        if (currentIndex - 1 >= 0) {
            playTrackAt(currentIndex - 1)
        }
    }

    private fun buildNotification(): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Аудио воспроизведение")
            .setContentText("Трек играет")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSessionCompat.sessionToken)
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
                description = "Фоновое воспроизведение"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        playerManager.release()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "audio_playback_channel"
        private const val NOTIFICATION_ID = 1
    }
}