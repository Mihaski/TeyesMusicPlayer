package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CategoryTileCard(
    tile: CategoryTile,
    selected: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    size: Dp
) {
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null

    Card(
        modifier = Modifier
            .size(size)
            .combinedClickable(onClick = onClick, onLongClick = onLongPress),
        border = border
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Обложка-превью (можно ваш кэш/плейсхолдер)
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Image(...) если есть tile.previewArt
            }
            Spacer(Modifier.height(8.dp))
            Text(tile.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                "${tile.count}",
                style = MaterialTheme.typography.labelMedium,
                color = LocalContentColor.current.copy(.7f)
            )
        }
    }
}