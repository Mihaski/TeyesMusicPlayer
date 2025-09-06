package com.nuclearcode.teyesmusicplayer.operatefield

data class CategoriesUiState(
    val tiles: List<CategoryTile> = emptyList(),
    val query: String = "",
    val grid: Boolean = true,
    val tileSize: TileSize = TileSize.Medium,
    val sort: SortOption = SortOption.ByNameAsc,
    val selected: Set<CategoryType> = emptySet(),
    val selectionMode: Boolean = false,
    val filters: Set<QuickFilter> = emptySet()
)