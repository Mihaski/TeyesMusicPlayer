package com.nuclearcode.teyesmusicplayer.ui

import android.util.Log
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuclearcode.teyesmusicplayer.ui.theme.Pink40


@Composable
@Preview
fun AudioPlayerExpandableBottomPanelPreview() {
    AudioPlayerExpandableBottomPanel(
        modifier = Modifier.background(Color.White),
        isExpanded = false,
        onExpandChange = { },
        isPlaying = false,
        progress = 0L,
        duration = 100L,
        onSeek = { },
        onPlayPause = { },
        onStop = { },
        onNext = { },
        onPrevious = { },
    )
}

@Composable
fun AudioPlayerExpandableBottomPanel(
    modifier: Modifier = Modifier,
    title: String = "Cause: don't passing parameter",
    artist: String = "Cause: don't passing parameter",
    byteArrayCover: ByteArray? = null,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    isPlaying: Boolean,
    progress: Long,
    duration: Long,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Long) -> Unit,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
) {
    val screenHeightDp = LocalWindowInfo.current.containerSize.height.dp

    val transition = updateTransition(targetState = isExpanded, label = "sheetTransition")

    val navigationInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val panelHeight by transition.animateDp(label = "panelHeight") { expanded ->
        if (expanded) screenHeightDp else 130.dp
    }

    LaunchedEffect(Unit) {
        Log.d("BottomPadding", "Bottom inset = $navigationInset,$screenHeightDp")
    }
    Box(
        modifier
            .fillMaxSize()
    ) {
        // Нижняя панель плеера
        Box(
            Modifier
                .then(if (!isExpanded) Modifier.offset(y = navigationInset) else Modifier)
                .height(panelHeight)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Pink40)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount < -20) onExpandChange(true)
                        if (dragAmount > 20) onExpandChange(false)
                    }
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (!isExpanded) {
                        onExpandChange(true)
                    }
                },
        ) {
            if (isExpanded) {// Полноэкранный плеер
                PlayerScreen( // todo enter interactive data
                    title = title,
                    artist = artist,
                    byteArrayCover = byteArrayCover,
                    progress = progress,
                    duration = duration,
                    isPlaying = isPlaying,
                    onPlayPause = { onPlayPause() },
                    onNext = { onNext() },
                    onPrevious = { onPrevious() },
                    onSeek = { onSeek(it) },
                    onLike = { }
                )
            } else { //Свернутое состояние
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                ) {
                    MiniPlayer(
                        title = title,
                        isPlaying = isPlaying,
                        progress = progress,
                        duration = duration,
                        onSeek = { onSeek(it) },
                        onPlayPause = { onPlayPause() },
                        onStop = { onStop() }
                    )
                }
            }
        }
    }
}