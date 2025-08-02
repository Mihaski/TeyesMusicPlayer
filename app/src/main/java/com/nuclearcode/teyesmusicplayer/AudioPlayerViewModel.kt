package com.nuclearcode.teyesmusicplayer

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
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

    private var currentPlaylist: List<AudioFile> = emptyList()
    private var currentIndex = 0

    init {
        playerManager.onTrackEnded = {
            playNext()
        }
    }

    fun playPrevious() {
        if (currentIndex - 1 >= 0) {
            currentIndex--
            val previousTrack = currentPlaylist[currentIndex]
            _nowPlaying.value = previousTrack
            playerManager.play(previousTrack)
        } else {
            // уже в начале — либо перезапуск, либо ничего
            playerManager.seekTo(0L) // просто перемотка в начало текущего
        }
    }

    fun playNext() {
        if (currentIndex + 1 < currentPlaylist.size) {
            currentIndex++
            val nextTrack = currentPlaylist[currentIndex]
            _nowPlaying.value = nextTrack
            playerManager.play(nextTrack)
        } else {
            stop() // конец плейлиста
        }
    }

//    fun playPlaylist(files: List<AudioFile>, startIndex: Int) {
//        currentPlaylist = files
//        currentIndex = startIndex
//        _nowPlaying.value = files.getOrNull(startIndex)
//        playerManager.setPlaylist(files, startIndex)
//    }

    fun togglePlayPause() {
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

    fun playPlaylist(files: List<AudioFile>, startIndex: Int, context: Context) {
        currentPlaylist = files
        currentIndex = startIndex
        val track = files.getOrNull(startIndex) ?: return
        _nowPlaying.value = track
        playerManager.setPlaylistInManager(files, startIndex)

        // Запуск сервиса
        val serviceIntent = Intent(context, AudioPlaybackService::class.java).apply {
            putParcelableArrayListExtra("PLAYLIST", ArrayList(files))
            putExtra("START_INDEX", startIndex)
        }

        ContextCompat.startForegroundService(context, serviceIntent)
    }


    fun sendCommand(context: Context, command: PlaybackCommand) {
        val intent = Intent(context, AudioPlaybackService::class.java).apply {
            action = command.name
        }
        context.startService(intent)
    }
}