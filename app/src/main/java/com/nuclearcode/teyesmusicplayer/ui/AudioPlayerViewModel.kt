package com.nuclearcode.teyesmusicplayer.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AudioPlayerViewModel @Inject constructor(
    private val repository: AudioRepository,
    playerManager: AudioPlayerManager,
    private val serviceConnection: AudioServiceConnection,
) : ViewModel() {
    val audioFiles = repository.audioFiles
    val nowPlaying: StateFlow<AudioFile?> = playerManager.nowPlaying
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val progress: StateFlow<Long> = playerManager.progressFlow
    val duration = playerManager.durationFlow
    fun play(audioFile: AudioFile) {
        val files = audioFiles.value
        val startIndex = files.indexOfFirst { it.id == audioFile.id }
        if (startIndex != -1) {
            serviceConnection.send(PlaybackCommand.Play(files, startIndex))
        }
    }
    fun stop() {
        serviceConnection.send(PlaybackCommand.Stop)
    }
    fun playNext() {
        serviceConnection.send(PlaybackCommand.PlayNext)
    }
    fun playPrevious() {
        serviceConnection.send(PlaybackCommand.PlayPrevious)
    }
    fun seekTo(position: Long) {
        serviceConnection.send(PlaybackCommand.SeekTo(position))
    }
    fun resumePause() {
        if (isPlaying.value) {
            serviceConnection.send(PlaybackCommand.Pause)
        } else {
            serviceConnection.send(PlaybackCommand.Resume)
        }
    }

    override fun onCleared() {
        repository.unregister()
        super.onCleared()
    }
}