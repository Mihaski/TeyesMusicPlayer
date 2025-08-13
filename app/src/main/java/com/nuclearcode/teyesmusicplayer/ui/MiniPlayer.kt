package com.nuclearcode.teyesmusicplayer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuclearcode.teyesmusicplayer.R

@Composable
@Preview
fun MiniPlayerPreview() {

    MiniPlayer(
        isPlaying = false,
        progress = 0L,
        duration = 100L,
        onSeek = { 1L },
        onPlayPause = { },
        onStop = { }
    )
}

@Composable
fun MiniPlayer(
    title: String = "Cause: don't passing parameter",
    isPlaying: Boolean,
    progress: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    onPlayPause: () -> Unit,
    onStop: () -> Unit
) {
    val safeDuration = duration.takeIf { it > 0 } ?: 1L
    val safeProgress = progress.coerceIn(0L, safeDuration)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                title,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = onPlayPause) {
                Icon(
                    if (isPlaying) ImageVector.vectorResource(
                        R.drawable.round_pause_24
                    ) else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
            IconButton(onClick = onStop) {
                Icon(
                    ImageVector.vectorResource(
                        R.drawable.round_stop_24
                    ), contentDescription = null
                )
            }
        }

        var internalProgress by remember {
            mutableLongStateOf(
                progress.coerceIn(
                    0L,
                    duration.coerceAtLeast(1L)
                )
            )
        }
        LaunchedEffect(progress) {
            if (internalProgress != progress) {
                internalProgress = progress
            }
        }

        AlignedSlider(internalProgress, safeDuration) { new ->
            internalProgress = new
            onSeek(new)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatTime(safeProgress))
            Text(formatTime(safeDuration))
        }
    }
}
