package com.nuclearcode.teyesmusicplayer.utility

import com.nuclearcode.teyesmusicplayer.ui.AudioFile

sealed class PlaybackCommand {
    data class Play(val playlist: List<AudioFile>, val index: Int) : PlaybackCommand()
    object Pause : PlaybackCommand()
    object Resume : PlaybackCommand()
    object Stop : PlaybackCommand()
    object PlayNext : PlaybackCommand()
    object PlayPrevious : PlaybackCommand()
    data class SeekTo(val position: Long) : PlaybackCommand()
}
