package com.nuclearcode.teyesmusicplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class AudioPlayerViewModelFactory @Inject constructor(
    private val player: AudioPlayerManager,
    private val repository: AudioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioPlayerViewModel::class.java)) {
            return AudioPlayerViewModel(player, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}