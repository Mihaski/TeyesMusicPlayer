package com.nuclearcode.teyesmusicplayer.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.ui.AudioPlayerViewModel

@UnstableApi
@Composable
fun MCategoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel
) {
    val filteredTracks by viewModel.filteredAudioFiles.collectAsState()
    val nowPlaying by viewModel.nowPlaying.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LazyColumn(
        modifier = modifier
    ) {


    }
}