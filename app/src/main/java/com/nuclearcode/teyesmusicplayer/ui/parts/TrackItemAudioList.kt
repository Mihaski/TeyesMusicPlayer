package com.nuclearcode.teyesmusicplayer.ui.parts

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.AudioFile

@Composable
@Preview
private fun TrackItemAudioListPreview() {

    TrackItemAudioList(
        file = AudioFile(
            "",
            1L,
            0,
            "sdasd",
            "sdasdad",
            "asdasd",
            1L,
            1L,
            null,
        ), isActive = false,
        isPlaying = false,
        onClick = {}
    )
}
@Composable
fun TrackItemAudioList(
//    title: String, todo
//    artists: String, todo
//    artists: ByteArray?, todo
    file: AudioFile,
    isActive: Boolean, // текущий выбранный трек
    isPlaying: Boolean, // играет или на паузе
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (file.embeddedArt != null) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(
                    file.embeddedArt, 0, file.embeddedArt.size
                ).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.teyes_banner_1024x1024),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(file.title, style = MaterialTheme.typography.titleMedium)
            Text(file.artist, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        if (isActive) {
            Icon(
                imageVector = if (!isPlaying) {
                    ImageVector.vectorResource(R.drawable.round_pause_24)
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}