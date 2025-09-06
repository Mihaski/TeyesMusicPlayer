package com.nuclearcode.teyesmusicplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.ui.AudioPlayerViewModel

@UnstableApi
@Composable
fun DirectorySelectorScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel
) {
    val dirs by viewModel.directories.collectAsState()
    val selectedDirs by viewModel.selectedDirsForSetting.collectAsState()

    LazyColumn(modifier = modifier) {
        items(dirs) { dir ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleDirectory(dir) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = dir in selectedDirs,
                    onCheckedChange = { viewModel.toggleDirectory(dir) }
                )
                Text(
                    text = dir.substringAfterLast("/"),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}