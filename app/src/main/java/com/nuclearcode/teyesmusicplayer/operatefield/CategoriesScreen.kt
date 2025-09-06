package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    MaterialTheme {
        CategoriesScreen(
            state = CategoriesUiState(
                tiles = listOf(
                    CategoryTile(CategoryType.Albums, "Альбомы", 10, null, "albums"),
                    CategoryTile(CategoryType.Artists, "Исполнители", 5, null, "artists")
                )
            ),
            onQueryChange = {},
            onToggleGrid = {},
            onTileSizeChange = {},
            onSortClick = {},
            onFilterToggle = {},
            onTileClick = {},
            onTileLongPress = {},
            onClearSelection = {},
            onBatchAction = {}
        )
    }
}
@UnstableApi
@Composable
fun CategoriesScreen(
    state: CategoriesUiState,
    onQueryChange: (String) -> Unit,
    onToggleGrid: () -> Unit,
    onTileSizeChange: (TileSize) -> Unit,
    onSortClick: () -> Unit,
    onFilterToggle: (QuickFilter) -> Unit,
    onTileClick: (CategoryTile) -> Unit,
    onTileLongPress: (CategoryTile) -> Unit,
    onClearSelection: () -> Unit,
    onBatchAction: (BatchAction) -> Unit,
) {
    val filtered = remember(state.tiles, state.query, state.filters, state.sort) {
        state.tiles
            .filter { it.title.contains(state.query, ignoreCase = true) }
            .let { list ->
                if (QuickFilter.FoldersOnly in state.filters) list.filter { it.type == CategoryType.Folders } else list
            }
            .sortedWith(
                when (state.sort) {
                    SortOption.ByNameAsc -> compareBy { it.title.lowercase() }
                    SortOption.ByNameDesc -> compareByDescending { it.title.lowercase() }
                    SortOption.ByCountDesc -> compareByDescending { it.count }
                    SortOption.ByRecent -> compareByDescending { it.type == CategoryType.RecentlyAdded }
                }
            )
    }

    Scaffold(
        topBar = {
            CategoriesTopBar(
                query = state.query,
                onQueryChange = onQueryChange,
                grid = state.grid,
                onToggleGrid = onToggleGrid,
                tileSize = state.tileSize,
                onTileSizeChange = onTileSizeChange,
                onSortClick = onSortClick,
                filters = state.filters,
                onFilterToggle = onFilterToggle,
                selectionMode = state.selectionMode,
                selectedCount = state.selected.size,
                onClearSelection = onClearSelection
            )
        },
        bottomBar = {
            if (state.selectionMode) {
                SelectionBottomBar(
                    selectedCount = state.selected.size,
                    onBatchAction = onBatchAction
                )
            }
        }
    ) { padding ->
        if (state.grid) {
            CategoryGrid(
                items = filtered,
                tileSize = state.tileSize,
                modifier = Modifier.padding(padding),
                onClick = onTileClick,
                onLongPress = onTileLongPress,
                selected = state.selected
            )
        } else {
            CategoryList(
                items = filtered,
                modifier = Modifier.padding(padding),
                onClick = onTileClick,
                onLongPress = onTileLongPress,
                selected = state.selected
            )
        }
    }
}
