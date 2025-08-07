package com.nuclearcode.teyesmusicplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class AudioPlayerViewModelFactory @Inject constructor(
    private val repository: AudioRepository,
    private val player: AudioPlayerManager,
    private val serviceConnection: AudioServiceConnection,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioPlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AudioPlayerViewModel(repository, player, serviceConnection) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}