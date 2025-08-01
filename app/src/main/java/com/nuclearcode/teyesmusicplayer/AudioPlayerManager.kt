package com.nuclearcode.teyesmusicplayer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class AudioPlayerManager @Inject constructor(
    private val context: Context
) {
    val exoPlayer = ExoPlayer.Builder(context).build()
    private var playlist: List<AudioFile> = emptyList()
    private var currentIndex = 0

    var onTrackEnded: (() -> Unit)? = null

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    onTrackEnded?.invoke()
                }
            }
        })
    }

    private var serviceStarted = false

    private fun startForegroundService() {
        val intent = Intent(context, AudioPlaybackService::class.java).apply {
            putExtra(
                "AUDIO_URI",
                exoPlayer.currentMediaItem?.localConfiguration?.uri.toString().toUri()
            )
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun setPlaylist(files: List<AudioFile>, startIndex: Int = 0) {
        playlist = files
        currentIndex = startIndex
        play(playlist.getOrNull(currentIndex) ?: return)

        if (!serviceStarted) {
            startForegroundService()
            serviceStarted = true
        }
    }

    private var currentFile: AudioFile? = null

    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private var progressJob: Job? = null

    fun play(file: AudioFile) {
        if (file == currentFile) {
            if (!exoPlayer.isPlaying) {
                exoPlayer.play()
                _isPlaying.value = true
            }
            return
        }

        val mediaItem = MediaItem.fromUri(Uri.fromFile(File(file.path)))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _isPlaying.value = true
        currentFile = file

        startProgressUpdates()
    }

    fun pause() {
        exoPlayer.pause()
        _isPlaying.value = false
    }

    fun resume() {
        exoPlayer.play()
        _isPlaying.value = true
    }

    fun stop() {
        exoPlayer.stop()
        _isPlaying.value = false
        progressJob?.cancel()
        _progress.value = 0
        currentFile = null // <-- ВАЖНО: сброс текущего файла
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun isPlaying(): Boolean = exoPlayer.isPlaying

    fun duration(): Long = exoPlayer.duration

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val currentPosition = withContext(Dispatchers.Main) {
                    exoPlayer.currentPosition
                }
                _progress.value = currentPosition
                delay(1000)
            }
        }
    }

    fun release() {
        stop()
        exoPlayer.release()
    }
}
