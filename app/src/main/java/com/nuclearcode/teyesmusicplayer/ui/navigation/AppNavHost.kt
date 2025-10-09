package com.nuclearcode.teyesmusicplayer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nuclearcode.teyesmusicplayer.ui.screens.DirectorySelectorScreen
import com.nuclearcode.teyesmusicplayer.ui.screens.AudioListScreen
import com.nuclearcode.teyesmusicplayer.ui.AudioPlayerViewModel
import com.nuclearcode.teyesmusicplayer.ui.CategoryHolder
import com.nuclearcode.teyesmusicplayer.ui.screens.FavoritesScreen
import com.nuclearcode.teyesmusicplayer.ui.screens.categories.FoldersScreen
import com.nuclearcode.teyesmusicplayer.ui.screens.categories.MainLibraryScreen
import com.nuclearcode.teyesmusicplayer.utility.PermissionHandler

@UnstableApi
@Composable
fun AppNavHost(
    audioViewModel: AudioPlayerViewModel
) {
    val backStack = rememberNavBackStack(
        NavigationItems.MainScreen(CategoryHolder.AllCategories)
    )
    var isExpandedHost by remember { mutableStateOf(false) }

    PermissionHandler(
        onPermissionGranted = {
            audioViewModel.refreshAudioFiles()
        }
    )

    Scaffold(
        bottomBar = {
            if (!isExpandedHost) {
                BottomBar(
                    backStack = backStack
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = if (isExpandedHost) {
                Modifier // без паддинга
            } else {
                Modifier.padding(innerPadding)
            },
            onBack = { backStack.removeLastOrNull() },
            entryProvider =
                entryProvider {
                    entry<NavigationItems.MainScreen> { category ->
                        when (category.categoryHolder) {
                            CategoryHolder.Albums -> FavoritesScreen()
                            CategoryHolder.AllTracks -> {
                                audioViewModel.resetDirectory()
                                backStack.add(NavigationItems.PlayList("all_tracks"))
                            } //todo после клика на этот айтем нет предиктив анимации, а на других есть
                            CategoryHolder.Authors -> FavoritesScreen()
                            CategoryHolder.Favorites -> backStack.add(NavigationItems.PlayList("favorites"))

                            //todo вроде чтобы открывать только тот плейлист который отфильтровался надо что то передавать
                            // сейчас внутри аудиоЛистСкрин фильтрованые треки, можно вытащить их сюда - но как будто не охото, но вью модель тут и так есть как будто можно
                            // а можно подумать скорее всего передавать не лист аудифайл а категори холдер и от него принимать решения как фильтровать

                            CategoryHolder.Folders -> FoldersScreen(viewModel = audioViewModel)
                            { backStack.add(NavigationItems.PlayList("folders")) }

                            CategoryHolder.Genres -> FavoritesScreen()
                            CategoryHolder.Years -> FavoritesScreen()
                            CategoryHolder.AllCategories -> MainLibraryScreen() { category ->
                                backStack.add(NavigationItems.MainScreen(category))
                            }
                        }
                    }
                    entry<NavigationItems.PlayList> { filter ->
                        when (filter.filter) { //todo тут пока не доработан подхват треков пока не могу выделить что то общее, added сделал в других местах если так и останется то можно просто вызывать функцию экрана аудиолист скрин
                            "favorites" -> AudioListScreen(
                                viewModel = audioViewModel,
                                isExpandedHost = isExpandedHost,
                                onExpandChangeHost = { isExpandedHost = it },
                            )

                            "folders" -> AudioListScreen(
                                viewModel = audioViewModel,
                                isExpandedHost = isExpandedHost,
                                onExpandChangeHost = { isExpandedHost = it },
                            )

                            "all_tracks" -> AudioListScreen(
                                viewModel = audioViewModel,
                                isExpandedHost = isExpandedHost,
                                onExpandChangeHost = { isExpandedHost = it },
                            )

                            "nothing" -> AudioListScreen(
                                viewModel = audioViewModel,
                                isExpandedHost = isExpandedHost,
                                onExpandChangeHost = { isExpandedHost = it },
                            )
                        }
                    }
                    entry<NavigationItems.ReadsFields> {
                        FavoritesScreen()
                    }
                    entry<NavigationItems.Favorites> {
                        FavoritesScreen()
                    }
                    entry<NavigationItems.SearchPlaces> {
                        DirectorySelectorScreen(viewModel = audioViewModel)
                    }
                }
        ) // navDisplay ended
    }
}