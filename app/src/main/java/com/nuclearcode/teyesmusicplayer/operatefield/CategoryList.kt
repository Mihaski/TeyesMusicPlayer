package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ListItem

@Composable
fun CategoryList(
    items: List<CategoryTile>,
    selected: Set<CategoryType>,
    modifier: Modifier = Modifier,
    onClick: (CategoryTile) -> Unit,
    onLongPress: (CategoryTile) -> Unit
) {
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items, key = { it.type }) { tile ->
            ListItem(
                headlineContent = { Text(tile.title) },
                supportingContent = { Text("${tile.count}") },
                leadingContent = {
                    Box(
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                },
                trailingContent = {
                    if (tile.type in selected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = { onClick(tile) },
                        onLongClick = { onLongPress(tile) }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Divider()
        }
    }
}

