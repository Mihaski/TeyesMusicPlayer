package com.nuclearcode.teyesmusicplayer.ui

import androidx.compose.ui.graphics.Color
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.navigation.ColorAsLongSerializer
import com.nuclearcode.teyesmusicplayer.ui.theme.categoriesColor
import kotlinx.serialization.Serializable

@Serializable
sealed class CategoryHolder(
    val title: String,
    val iconRes: Int,
    val colorValue: Long
) {
    @Serializable
    data object AllTracks : CategoryHolder(
        "Все треки",
        R.drawable.key_score_sol_tone_g_svgrepo_com,
        categoriesColor.component1()
    )

    @Serializable
    data object Folders : CategoryHolder(
        "Папки",
        R.drawable.baseline_folder_24,
        categoriesColor.component2(),
    )

    @Serializable
    data object Favorites : CategoryHolder(
        "Избранное",
        R.drawable.rounded_favorite_24,
        categoriesColor.component3(),
    )

    @Serializable
    data object Albums : CategoryHolder(
        "Альбомы",
        R.drawable.guitar_instrument_electric_flying_v_svgrepo_com,
        categoriesColor.component4()
    )

    @Serializable
    data object Authors : CategoryHolder(
        "Исполнители",
        R.drawable.baseline_mic_24,
        categoriesColor.component5(),
    )

    @Serializable
    data object Genres : CategoryHolder(
        "Жанры",
        R.drawable.action_gesture_rock_metal_svgrepo_com,
        categoriesColor.elementAt(5),
    )

    @Serializable
    data object Years : CategoryHolder(
        "Годы",
        R.drawable.outline_kitesurfing_24,
        categoriesColor.elementAt(6),
    )

    @Serializable
    data object AllCategories : CategoryHolder( //invisible category
        "Все категории",
        R.drawable.key_score_sol_tone_g_svgrepo_com,
        categoriesColor.elementAt(7)
    )
}
