package com.nuclearcode.teyesmusicplayer

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
@Preview
fun AudioListScreenPreview() {//todo удалить т.к. не рендериться

    val viewModel = viewModel<AudioPlayerViewModel>()

    AudioListScreen(
        Modifier,
        viewModel
    )
}

@Composable
fun AudioListScreen(
    modifier: Modifier,
    viewModel: AudioPlayerViewModel
) {
    val tracks by viewModel.audioFiles.collectAsState()
    val nowPlaying by viewModel.nowPlaying.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val duration = nowPlaying?.let { viewModel.getDuration() } ?: 0L
    val isPlaying by viewModel.isPlaying.collectAsState()
    val context = LocalContext.current

    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (isExpanded) 0.dp else 80.dp)
        ) {
            items(tracks) { track ->
                TrackItem(
                    file = track,
                    isActive = nowPlaying?.path == track.path,
                    isPlaying = nowPlaying?.path == track.path && isPlaying,
                    onClick = { viewModel.
                        playPlaylist(tracks, track.id, context)
                    }
                )
            }
        }

        AudioPlayerExpandableBottomPanel(
            title = nowPlaying?.title ?: "Cause: collectAsState",
            artist = nowPlaying?.artist ?: "Cause: collectAsState",
            isExpanded = isExpanded,
            onExpandChange = { isExpanded = it },
            byteArrayCover = nowPlaying?.embeddedArt,
            isPlaying = isPlaying,
            progress = progress,
            duration = duration,
            onSeek = { viewModel.seekTo(it) },
            onPlayPause = { viewModel.togglePlayPause() },
            onStop = { viewModel.stop() },
            onNext = { viewModel.playNext() },
            onPrevious = { viewModel.playPrevious() },
        )
    }
}

