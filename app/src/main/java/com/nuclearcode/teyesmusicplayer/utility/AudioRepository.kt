package com.nuclearcode.teyesmusicplayer.utility

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.nuclearcode.teyesmusicplayer.ui.AudioFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AudioRepository @Inject constructor(private val context: Context) {

    private val _audioFiles = MutableStateFlow<List<AudioFile>>(emptyList())
    val audioFiles: StateFlow<List<AudioFile>> = _audioFiles

    private val _directories = MutableStateFlow<List<String>>(emptyList())
    val directories: StateFlow<List<String>> = _directories

    private val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            loadAudioFiles()
        }
    }

    init {
        context.contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            true,
            observer
        )
        loadAudioFiles()
    }

    fun unregister() {
        context.contentResolver.unregisterContentObserver(observer)
    }

    private fun loadAudioFiles() {
        val list = getAudioFiles(context)
        _audioFiles.value = list

        // Собираем директории из списка аудиофайлов
        val dirs = list.map { it.path.substringBeforeLast("/") }
            .toSet()
            .toList()
            .sorted()
        _directories.value = dirs
    }
}