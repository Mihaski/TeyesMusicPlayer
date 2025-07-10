package com.nuclearcode.teyesmusicplayer

data class AudioFile(
    val title: String,
    val artist: String?,
    val path: String,
    val duration: Long,
)
