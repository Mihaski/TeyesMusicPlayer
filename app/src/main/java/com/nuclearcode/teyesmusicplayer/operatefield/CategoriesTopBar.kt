package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CategoriesTopBarPreview() {
    CategoriesTopBar(
        query = "",
        onQueryChange = { "" },
        grid = false,
        onToggleGrid = { },
        tileSize = TileSize.Compact,
        onTileSizeChange = { TileSize.Compact },
        onSortClick = { },
        filters = emptySet(),
        onFilterToggle = { QuickFilter.Favorites },
        selectionMode = false,
        selectedCount = 1,
        onClearSelection = { }
    )
}

@Composable
fun CategoriesTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    grid: Boolean,
    onToggleGrid: () -> Unit,
    tileSize: TileSize,
    onTileSizeChange: (TileSize) -> Unit,
    onSortClick: () -> Unit,
    filters: Set<QuickFilter>,
    onFilterToggle: (QuickFilter) -> Unit,
    selectionMode: Boolean,
    selectedCount: Int,
    onClearSelection: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (selectionMode) {
                Text("Выбрано: $selectedCount", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onClearSelection) { Text("Сбросить") }
            } else {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    placeholder = { Text("Поиск по категориям") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onToggleGrid) {
                    Icon(
                        if (grid) Icons.AutoMirrored.Filled.List else Icons.Filled.LocationOn,
                        null
                    )
                }
                IconButton(onClick = onSortClick) {
                    Icon(Icons.Outlined.Edit, null)
                }
                // Размер плитки (цикл)
                IconButton(onClick = {
                    val next = when (tileSize) {
                        TileSize.Compact -> TileSize.Medium
                        TileSize.Medium -> TileSize.Large
                        TileSize.Large -> TileSize.Compact
                    }
                    onTileSizeChange(next)
                }) {
                    Icon(Icons.Default.Add, null)
                }
            }
        }

        // Быстрые фильтры
        Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuickFilterChip("Папки", QuickFilter.FoldersOnly, filters, onFilterToggle)
            QuickFilterChip("Избранное", QuickFilter.Favorites, filters, onFilterToggle)
            QuickFilterChip("Локальные", QuickFilter.LocalOnly, filters, onFilterToggle)
        }
    }
}