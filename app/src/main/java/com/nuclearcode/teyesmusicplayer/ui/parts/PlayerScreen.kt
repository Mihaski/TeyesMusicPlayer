package com.nuclearcode.teyesmusicplayer.ui.parts

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
private fun PlayerPreview() {
    PlayerScreen(
        progress = 0,
        duration = 0,
        isPlaying = false,
        onPlayPause = {},
        onNext = {},
        onPrevious = {},
        onSeek = {},
        isFavorite = false,
        onFavorite = {},
    )
}

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    title: String = "Cause: don't passing parameter",
    artist: String = "Cause: don't passing parameter",
    byteArrayCover: ByteArray? = null,
    progress: Long,
    duration: Long,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Long) -> Unit,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {//horizontal
        LandscapePlayerScreen(
            modifier = modifier,
            progress = progress,
            duration = duration,
            title = title,
            artist = artist,
            byteArrayCover = byteArrayCover,
            isPlaying = isPlaying,
            onPlayPause = { onPlayPause() },
            onNext = { onNext() },
            onPrevious = { onPrevious() },
            onSeek = { onSeek(it) },
            isFavorite = isFavorite,
            onFavorite = { onFavorite() }
        )
    } else { // vertical
        PortraitPlayerScreen(
            modifier = modifier,
            progress = progress,
            duration = duration,
            title = title,
            artist = artist,
            byteArrayCover = byteArrayCover,
            isPlaying = isPlaying,
            onPlayPause = { onPlayPause() },
            onNext = { onNext() },
            onPrevious = { onPrevious() },
            onSeek = { onSeek(it) },
            isFavorite = isFavorite,
            onFavorite = { onFavorite() }
        )
    }
}
