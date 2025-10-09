package com.nuclearcode.teyesmusicplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.rpl.FavoriteRepository
import com.nuclearcode.teyesmusicplayer.utility.AudioPlayerManager
import com.nuclearcode.teyesmusicplayer.utility.AudioRepository
import com.nuclearcode.teyesmusicplayer.utility.AudioServiceConnection
import com.nuclearcode.teyesmusicplayer.utility.PlaybackCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@UnstableApi
class AudioPlayerViewModel @Inject constructor(
    private val repository: AudioRepository,
    playerManager: AudioPlayerManager,
    private val serviceConnection: AudioServiceConnection,
    private val repoFavorite: FavoriteRepository,
) : ViewModel() {
    val audioFiles = repository.audioFiles
    val nowPlaying: StateFlow<AudioFile?> = playerManager.nowPlaying
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val progress: StateFlow<Long> = playerManager.progressFlow
    val duration = playerManager.durationFlow
    val directories = repository.directories
    private val _selectedDirsForSetting = MutableStateFlow(directories.value.toSet())
    val selectedDirsForSetting: StateFlow<Set<String>> = _selectedDirsForSetting
    private val _selectedDirsForCategory = MutableStateFlow(directories.value.toSet())
    val selectedDirsForCategory: StateFlow<Set<String>> = _selectedDirsForCategory

    val favorites = repoFavorite.getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleFavorite(trackId: Long) {
        viewModelScope.launch {
            repoFavorite.toggleFavorite(trackId)
        }
    }

    suspend fun isFavorite(trackId: Long): Boolean {
        return repoFavorite.isFavorite(trackId)
    }

    val filteredAudioFilesForCategory: StateFlow<List<AudioFile>> =
        combine(
            audioFiles,
            selectedDirsForCategory,
            selectedDirsForSetting
        ) { allFiles, category, setting ->
            if (setting.isEmpty()) {
                allFiles
            } else {
                allFiles.filter { file ->
                    file.path.substringBeforeLast("/") in setting
                }.filter { fileSet ->
                    fileSet.path.substringBeforeLast("/") in category
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleCategoryDir(dir: String) {
        _selectedDirsForCategory.value = mutableSetOf(dir)
    }

    fun refreshAudioFiles() {
        repository.loadAudioFiles()
    }

    fun resetDirectory() {
        _selectedDirsForSetting.value = directories.value.toSet()
        _selectedDirsForCategory.value = directories.value.toSet()
    }

    fun toggleDirectory(dir: String) {
        _selectedDirsForSetting.value = _selectedDirsForSetting.value.toMutableSet().apply {
            if (contains(dir)) remove(dir) else add(dir)
        }
    }

    fun play(audioFile: AudioFile) {
        val files = audioFiles.value
        val startIndex = files.indexOfFirst { it.appId == audioFile.appId }
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