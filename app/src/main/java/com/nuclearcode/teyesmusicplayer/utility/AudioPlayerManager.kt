package com.nuclearcode.teyesmusicplayer.utility

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.nuclearcode.teyesmusicplayer.ui.AudioFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class AudioPlayerManager @Inject constructor(context: Context) {
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    private val _nowPlaying = MutableStateFlow<AudioFile?>(null)
    val nowPlaying: StateFlow<AudioFile?> get() = _nowPlaying
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying
    private val _durationFlow = MutableStateFlow(0L)
    val durationFlow: StateFlow<Long> = _durationFlow
    private val _progressFlow = MutableStateFlow(0L)
    val progressFlow: StateFlow<Long> = _progressFlow
    private var progressJob: Job? = null

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val position = withContext(Dispatchers.Main) {
                    exoPlayer.currentPosition
                }
                _progressFlow.value = position
                delay(1000L) // или меньше, если нужна более плавная анимация
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }
    var nextCallback: (() -> Unit)? = null

    init {
        exoPlayer.setAudioAttributes( // управление фокусом
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(), true
        )

        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(metadata: MediaMetadata) {
                _durationFlow.value = exoPlayer.duration.coerceAtLeast(0L)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) startProgressUpdates() else stopProgressUpdates()
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    // auto-play next
                    nextCallback?.invoke()
                }
                if (state == Player.STATE_READY) {
                    // get duration
                    _durationFlow.value = exoPlayer.duration.coerceAtLeast(0L)
                }
            }
        })
    }

    fun play(audioFile: AudioFile) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            val contentUri = audioFile.contentUri.toUri()
            val mediaItem = MediaItem.Builder()
                .setUri(contentUri) // лучше сразу хранить ContentUri
                .setMediaId(audioFile.id.toString())
                .build()

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            _nowPlaying.value = audioFile
        } else {
            val mediaItem = MediaItem.fromUri(Uri.fromFile(File(audioFile.path)))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            _nowPlaying.value = audioFile
        }
    }

    fun pause() = exoPlayer.pause()
    fun resume() = exoPlayer.play()
    fun stop() {
        exoPlayer.stop()
        _nowPlaying.value = null
        stopProgressUpdates()
    }

    fun release() {
        exoPlayer.release()
        _nowPlaying.value = null
        stopProgressUpdates()
    }
}