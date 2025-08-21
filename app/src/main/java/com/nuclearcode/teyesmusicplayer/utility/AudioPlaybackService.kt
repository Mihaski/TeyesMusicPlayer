package com.nuclearcode.teyesmusicplayer.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.nuclearcode.teyesmusicplayer.PlayerApp
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.AudioFile
import com.nuclearcode.teyesmusicplayer.ui.MainActivity
import javax.inject.Inject

@UnstableApi
class AudioPlaybackService : Service() {
    @Inject
    lateinit var playerManager: AudioPlayerManager
    private lateinit var playerNotificationManager: PlayerNotificationManager
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

        createNotificationChannel()

        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            CHANNEL_ID // 🔥 теперь тот же ID
        )
            .setSmallIconResourceId(R.drawable.ic_launcher_foreground) // 🔥 безопасная иконка
            .setMediaDescriptionAdapter(DescriptionAdapter())
            .setNotificationListener(NotificationListenerTeyes())
            .setChannelImportance(NotificationManager.IMPORTANCE_LOW)
            .build()
            .apply {
                setPlayer(playerManager.exoPlayer)
                setUseNextAction(true)
                setUsePreviousAction(true)
                setUseFastForwardAction(false)
                setUseRewindAction(false)
            }

//        startForeground(NOTIFICATION_ID, buildNotification())
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
        playerNotificationManager.setPlayer(null)
        playerManager.release()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "audio_playback_channel"
        private const val NOTIFICATION_ID = 1
    }

    private inner class DescriptionAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): String {
            return "Моя музыка" // Заголовок (имя трека)
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return PendingIntent.getActivity(
                this@AudioPlaybackService,
                0,
                Intent(this@AudioPlaybackService, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        override fun getCurrentContentText(player: Player): String? {
            return "Исполняется трек" // Доп. текст
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            // можно вернуть обложку альбома
            return BitmapFactory.decodeResource(resources, R.drawable.round_settings_24)
        }
    }

    private inner class NotificationListenerTeyes : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopSelf() // убираем сервис, если уведомление закрыто
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            startForeground(notificationId, notification)
        }
    }
}