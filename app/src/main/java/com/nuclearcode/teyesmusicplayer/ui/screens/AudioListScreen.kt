package com.nuclearcode.teyesmusicplayer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.ui.parts.AudioPlayerExpandableBottomPanel
import com.nuclearcode.teyesmusicplayer.ui.AudioPlayerViewModel
import com.nuclearcode.teyesmusicplayer.ui.parts.TrackItemAudioList

@UnstableApi
@Composable
fun AudioListScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel,
    isExpandedHost: Boolean,
    onExpandChangeHost: (Boolean) -> Unit,
) {
//    val tracks by viewModel.audioFiles.collectAsState() //todo delete
    val filteredTracks by viewModel.filteredAudioFiles.collectAsState()
    val nowPlaying by viewModel.nowPlaying.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val progress by viewModel.progress.collectAsState()


    var isExpanded by remember { mutableStateOf(isExpandedHost) }

    Box(
        modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(bottom = if (isExpanded) 0.dp else 100.dp)
        ) {
            items(filteredTracks) { track ->

                TrackItemAudioList(
                    file = track,
                    isActive = nowPlaying?.id == track.id,
                    isPlaying = isPlaying, //todo если криво работает иконка смотри сюда
                    onClick = {
                        viewModel.play(track)
                    }
                )
            }
        }

        AudioPlayerExpandableBottomPanel(
            title = nowPlaying?.title ?: "Cause: collectAsState",
            artist = nowPlaying?.artist ?: "Cause: collectAsState",
            isExpanded = isExpanded,
            onExpandChange = {
                isExpanded = it
                onExpandChangeHost(it)
            },
            byteArrayCover = nowPlaying?.embeddedArt,
            isPlaying = isPlaying,
            progress = progress,
            duration = duration,
            onSeek = { viewModel.seekTo(it) },
            onPlayPause = { viewModel.resumePause() },
            onStop = { viewModel.stop() },
            onNext = { viewModel.playNext() },
            onPrevious = { viewModel.playPrevious() },
        )
    }
}

