package com.nuclearcode.teyesmusicplayer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AudioPlayerViewModel @Inject constructor(
    private val playerManager: AudioPlayerManager,
    private val repository: AudioRepository,
) : ViewModel() {

    val audioFiles = repository.audioFiles
    private val _nowPlaying = MutableStateFlow<AudioFile?>(null)
    val nowPlaying: StateFlow<AudioFile?> = _nowPlaying
    val isPlaying: StateFlow<Boolean> get() = playerManager.isPlaying
    val progress: StateFlow<Long> get() = playerManager.progress

    fun play(file: AudioFile) {
        _nowPlaying.value = file
        playerManager.play(file)
    }

    fun togglePlayPause() {
        val current = _nowPlaying.value ?: return // todo удалить потим
        if (playerManager.isPlaying()) {
            playerManager.pause()
        } else {
            playerManager.resume()
        }
    }

    fun stop() {
        playerManager.stop()
        _nowPlaying.value = null
    }

    fun seekTo(position: Long) {
        playerManager.seekTo(position)
    }

    fun getDuration(): Long = playerManager.duration()

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
        repository.unregister()
    }
}
