package com.nuclearcode.teyesmusicplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 500L

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    private val updateRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    _progress.value = it.currentPosition
                    handler.postDelayed(this, updateInterval)
                }
            }
        }
    }

    fun playTrack(context: Context, filePath: String) {
        stopPlayback()

        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(filePath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            _duration.value = mediaPlayer?.duration ?: 0
            handler.post(updateRunnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _progress.value = 0
        _duration.value = 0
        handler.removeCallbacks(updateRunnable)
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _progress.value = position
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}