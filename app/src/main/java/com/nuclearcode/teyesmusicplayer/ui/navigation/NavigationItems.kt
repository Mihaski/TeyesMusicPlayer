package com.nuclearcode.teyesmusicplayer.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.CategoryHolder
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationItems(
    val label: String, val iconRes: Int, val route: String //todo delete route
) : NavKey {
    @Serializable
    data class MainScreen(val categoryHolder: CategoryHolder) : NavigationItems(
        label = "Выбор ini плей-листа",
        iconRes = R.drawable.rounded_format_list_bulleted_24,
        route = "reads_field",
    )
    @Serializable
    data class PlayList(val filter: String = "nothing") : NavigationItems(
        label = "Плей-лист",
        iconRes = R.drawable.round_playlist_play_24,
        route = "play_list",
    )
    @Serializable
    data object Favorites : NavigationItems(
        label = "Избранное",
        iconRes = R.drawable.rounded_favorite_24,
        route = "favorites",
    )
    @Serializable
    data object SearchPlaces : NavigationItems(
        label = "МестаПоиска",
        iconRes = R.drawable.rounded_bookmark_manager_24,
        route = "search_place",
    )
    @Serializable
    data object ReadsFields : NavigationItems(
        label = "ОбластьЧтения",
        iconRes = R.drawable.rounded_format_list_bulleted_24,
        route = "reads_field",
    )
}
