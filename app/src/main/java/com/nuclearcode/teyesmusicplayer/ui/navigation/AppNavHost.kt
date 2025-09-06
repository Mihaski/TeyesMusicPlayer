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
    val backStack = rememberNavBackStack(NavigationItems.MainScreen(CategoryHolder.AllCategories))
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
                    entry<NavigationItems.MainScreen> {

                        when (it.categoryHolder) {
                            CategoryHolder.Albums -> TODO()
                            CategoryHolder.AllTracks -> TODO()
                            CategoryHolder.Authors -> TODO()
                            CategoryHolder.Favorites -> TODO()
                            CategoryHolder.Folders -> FoldersScreen(viewModel = audioViewModel)
                            { backStack.add(NavigationItems.PlayList) }

                            CategoryHolder.Genres -> TODO()
                            CategoryHolder.Years -> TODO()
                            CategoryHolder.AllCategories -> MainLibraryScreen() { category ->
                                backStack.add(NavigationItems.MainScreen(category))
                            }
                        }
                    }
                    entry<NavigationItems.PlayList> {
                        AudioListScreen(
                            viewModel = audioViewModel,
                            isExpandedHost = isExpandedHost,
                            onExpandChangeHost = { isExpandedHost = it },
                        )
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