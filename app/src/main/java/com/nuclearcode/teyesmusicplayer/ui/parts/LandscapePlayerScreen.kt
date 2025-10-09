package com.nuclearcode.teyesmusicplayer.ui.parts

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.theme.PurpleGrey40
import com.nuclearcode.teyesmusicplayer.ui.theme.PurpleGrey80
import com.nuclearcode.teyesmusicplayer.utility.formatTime

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun PreviewLandscapePlayerScreen() {
    LandscapePlayerScreen(
        progress = 0,
        duration = 0,
        isPlaying = false,
        onPlayPause = { },
        onNext = { },
        onPrevious = { },
        onSeek = { },
        isFavorite = false,
        onFavorite = { }
    )
}

@Composable
fun LandscapePlayerScreen(
    modifier: Modifier = Modifier,
    progress: Long,
    duration: Long,
    title: String = "Cause: don't passing parameter",
    artist: String = "Cause: don't passing parameter",
    byteArrayCover: ByteArray? = null,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Long) -> Unit,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
) {
    val safeDuration = duration.takeIf { it > 0 } ?: 1L
    val safeProgress = progress.coerceIn(0L, safeDuration)

    Row( // main container
        modifier = modifier
            .fillMaxSize()
            .then(
                if (byteArrayCover == null) Modifier.background(PurpleGrey40)
                else Modifier.background(PurpleGrey80)
            )
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (byteArrayCover != null) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(
                    byteArrayCover,
                    0,
                    byteArrayCover.size
                ).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.5f)
                    .clip(RoundedCornerShape(35.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.teyes_banner_1024x1024),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(35.dp))
            )
        }
        Spacer(Modifier.width(16.dp))

        Column {  // right part
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artist,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 1,
            )

            Spacer(Modifier.height(16.dp))

            // Прогресс
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
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(safeProgress),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatTime(safeDuration),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(24.dp))

            // Кнопки управления
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* Повтор */ }) {
                    Icon(
                        painter = painterResource(R.drawable.rounded_repeat_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                IconButton(onClick = onPrevious) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                IconButton(onClick = onPlayPause) {
                    Icon(
                        if (isPlaying) ImageVector.vectorResource(
                            R.drawable.round_pause_24
                        )
                        else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }

                IconButton(onClick = onNext) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                IconButton(onClick =  onFavorite ) {
                    if (isFavorite) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Blue
                        )
                    } else {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        } // end of right part
    } // end of main container
}