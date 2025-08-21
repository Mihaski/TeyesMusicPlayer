package com.nuclearcode.teyesmusicplayer.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.nuclearcode.teyesmusicplayer.PlayerApp
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.AudioFile
import com.nuclearcode.teyesmusicplayer.ui.MainActivity
import javax.inject.Inject

class AudioPlaybackService : Service() {

    @Inject
    lateinit var playerManager: AudioPlayerManager

    private lateinit var mediaSessionCompat: MediaSessionCompat
    private var playlist: List<AudioFile> = emptyList()
    private var currentIndex = 0
    private lateinit var notificationManager: NotificationManager

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

    override fun onBind(intent: Intent?): IBinder = AudioServiceBinder()

    override fun onCreate() {
        super.onCreate()
        (application as PlayerApp).appComponent.injectService(this)

        mediaSessionCompat = MediaSessionCompat(this, "AudioSession")
        mediaSessionCompat.isActive = true

        playerManager.nextCallback = { playNext() }

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    private fun playTrackAt(index: Int) {
        if (index in playlist.indices) {
            currentIndex = index
            playerManager.play(playlist[currentIndex])
            startForeground(NOTIFICATION_ID, buildNotification())
        }
    }

    private fun playNext() {
        if (currentIndex + 1 < playlist.size) playTrackAt(currentIndex + 1)
    }

    private fun playPrevious() {
        if (currentIndex - 1 >= 0) playTrackAt(currentIndex - 1)
    }

    private fun buildNotification(): Notification {
        val intent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseAction = if (playerManager.exoPlayer.isPlaying) {
            NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                "Pause",
                PendingIntent.getService(
                    this,
                    0,
                    Intent(this, AudioPlaybackService::class.java).apply {
                        putExtra("COMMAND", "PAUSE")
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            NotificationCompat.Action(
                android.R.drawable.ic_media_play,
                "Play",
                PendingIntent.getService(
                    this,
                    0,
                    Intent(this, AudioPlaybackService::class.java).apply {
                        putExtra("COMMAND", "RESUME")
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        val nextAction = NotificationCompat.Action(
            android.R.drawable.ic_media_next,
            "Next",
            PendingIntent.getService(
                this,
                0,
                Intent(this, AudioPlaybackService::class.java).apply {
                    putExtra("COMMAND", "NEXT")
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        val prevAction = NotificationCompat.Action(
            android.R.drawable.ic_media_previous,
            "Previous",
            PendingIntent.getService(
                this,
                0,
                Intent(this, AudioPlaybackService::class.java).apply {
                    putExtra("COMMAND", "PREVIOUS")
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Аудио воспроизведение")
            .setContentText("Исполняется трек")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.round_settings_24))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setContentIntent(intent)
            .addAction(prevAction)
            .addAction(playPauseAction)
            .addAction(nextAction)
            .setStyle(MediaStyle().setMediaSession(mediaSessionCompat.sessionToken))
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager =
                getSystemService(NotificationManager::class.java) ?: return
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Аудио плеер",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Фоновое воспроизведение"
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        playerManager.release()
        super.onDestroy()
    }

    companion object {

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "audio_playback_channel"
    }
}
