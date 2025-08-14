package com.nuclearcode.teyesmusicplayer.ui.parts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
@Preview
fun AlignedSliderPreview() {
    var progress by remember { mutableLongStateOf(30L) }

    AlignedSlider(
        progress = progress,
        duration = 100,
        onSeek = { new -> progress = new }
    )
}

@Composable
fun AlignedSlider(
    progress: Long,
    duration: Long,
    onSeek: (Long) -> Unit
) {
    val thumbRadius = 6.dp
    val trackHeight = 2.dp

    var sliderWidth by remember { mutableIntStateOf(1) }
    var isDragging by remember { mutableStateOf(false) }
    var dragPos by remember { mutableFloatStateOf(0f) }

    // Вычисляем позицию для прогресса
    val targetPos = if (duration > 0 && sliderWidth > 0) {
        (progress.toFloat() / duration.toFloat()) * sliderWidth
    } else 0f

    // Используем анимацию только если не перетаскиеваем, иначе мгновенно меняем позицию
    val sliderPos = if (isDragging) {
        dragPos
    } else {
        animateFloatAsState(
            targetValue = targetPos,
            animationSpec = tween(durationMillis = 300)
        ).value
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thumbRadius * 2)
            .onGloballyPositioned {
                sliderWidth = it.size.width
            }
            .pointerInput(duration) {
                detectTapGestures { offset ->
                    val x = offset.x.coerceIn(0f, sliderWidth.toFloat())
                    onSeek(((x / sliderWidth) * duration).toLong().coerceIn(0L, duration))
                }
            }
            .pointerInput(duration) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragPos = offset.x.coerceIn(0f, sliderWidth.toFloat())
                    },
                    onDrag = { change, _ ->
                        dragPos += change.positionChange().x
                        dragPos = dragPos.coerceIn(0f, sliderWidth.toFloat())
                        onSeek(((dragPos / sliderWidth) * duration).toLong().coerceIn(0L, duration))
                    },
                    onDragEnd = {
                        isDragging = false
                        onSeek(((dragPos / sliderWidth) * duration).toLong().coerceIn(0L, duration))
                    },
                    onDragCancel = {
                        isDragging = false
                    }
                )
            }
    ) {
        // Track
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .height(trackHeight)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(1.dp))
        )

        // Thumb
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = sliderPos.roundToInt() - thumbRadius.roundToPx(),
                        y = 0
                    )
                }
                .size(thumbRadius * 2)
                .background(Color.Black, shape = CircleShape)
        )
    }
}