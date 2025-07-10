package com.nuclearcode.teyesmusicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AlignedSlider(
    progress: Float = 0f,
    duration: Float = 0f,
    onSeek: (Float) -> Unit = {},
) {
    val thumbRadius = 6.dp
    val trackHeight = 2.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thumbRadius * 2)// высота под thumb
    ) {
        // Track (полоса)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .height(trackHeight)
                .background(Color.Gray, RoundedCornerShape(1.dp))
        )

        // Thumb (ползунок)
        val positionFraction = if (duration == 0f) 0f else progress / duration

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = with(LocalDensity.current) {
                    (positionFraction * (LocalConfiguration.current.screenWidthDp.dp.toPx())).toDp()
                } - thumbRadius)
                .size(thumbRadius * 2)
                .background(Color.White, shape = CircleShape)
        )

        Slider(
            value = progress,
            onValueChange = { onSeek(it) },
            valueRange = 0f..duration,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp).padding(top = 30.dp), // регулируй под нужное
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            )
        )
    }
}