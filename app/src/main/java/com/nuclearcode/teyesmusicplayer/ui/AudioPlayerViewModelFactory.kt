package com.nuclearcode.teyesmusicplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.rpl.FavoriteRepository
import com.nuclearcode.teyesmusicplayer.utility.AudioPlayerManager
import com.nuclearcode.teyesmusicplayer.utility.AudioRepository
import com.nuclearcode.teyesmusicplayer.utility.AudioServiceConnection
import javax.inject.Inject

@UnstableApi
class AudioPlayerViewModelFactory @Inject constructor(
    private val repository: AudioRepository,
    private val player: AudioPlayerManager,
    private val serviceConnection: AudioServiceConnection,
    private val repoFavorite: FavoriteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioPlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AudioPlayerViewModel(repository, player, serviceConnection, repoFavorite) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}