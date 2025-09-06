package com.nuclearcode.teyesmusicplayer.operatefield

data class CategoryTile(
    val type: CategoryType,
    val title: String,
    val count: Int,
    val previewArt: Any? = null, // Bitmap, ImageBitmap или ваш кэш-ключ
    val route: String            // навигационный маршрут (Navigation 3 / ваш роутер)
)