package com.nuclearcode.teyesmusicplayer.ui

sealed class PlaybackCommand {
    data class Play(val playlist: List<AudioFile>, val index: Int) : PlaybackCommand()
    object Pause : PlaybackCommand()
    object Resume : PlaybackCommand()
    object Stop : PlaybackCommand()
    object PlayNext : PlaybackCommand()
    object PlayPrevious : PlaybackCommand()
    data class SeekTo(val position: Long) : PlaybackCommand()
}
