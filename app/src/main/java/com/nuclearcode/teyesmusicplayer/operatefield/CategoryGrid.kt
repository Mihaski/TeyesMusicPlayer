package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.media3.common.util.UnstableApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@UnstableApi
@Composable
fun CategoryGrid(
    items: List<CategoryTile>,
    tileSize: TileSize,
    selected: Set<CategoryType>,
    modifier: Modifier = Modifier,
    onClick: (CategoryTile) -> Unit,
    onLongPress: (CategoryTile) -> Unit
) {
    val minSize = when (tileSize) {
        TileSize.Compact -> 120.dp
        TileSize.Medium -> 160.dp
        TileSize.Large -> 200.dp
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.type }) { tile ->
            CategoryTileCard(
                tile = tile,
                selected = tile.type in selected,
                onClick = { onClick(tile) },
                onLongPress = { onLongPress(tile) },
                size = minSize
            )
        }
    }
}
