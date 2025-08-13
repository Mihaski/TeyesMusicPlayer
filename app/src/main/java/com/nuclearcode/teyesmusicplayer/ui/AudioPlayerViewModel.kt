package com.nuclearcode.teyesmusicplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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
    val directories = repository.directories
    private val _selectedDirs = MutableStateFlow<Set<String>>(emptySet())
    val selectedDirs: StateFlow<Set<String>> = _selectedDirs

    val filteredAudioFiles: StateFlow<List<AudioFile>> =
        combine(audioFiles, selectedDirs) { allFiles, selected ->
            if (selected.isEmpty()) {
                allFiles
            } else {
                allFiles.filter { file ->
                    file.path.substringBeforeLast("/") in selected
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleDirectory(dir: String) {
        _selectedDirs.value = _selectedDirs.value.toMutableSet().apply {
            if (contains(dir)) remove(dir) else add(dir)
        }
    }

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