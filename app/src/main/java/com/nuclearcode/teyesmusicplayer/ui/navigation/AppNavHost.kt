package com.nuclearcode.teyesmusicplayer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nuclearcode.teyesmusicplayer.ui.screens.DirectorySelector
import com.nuclearcode.teyesmusicplayer.ui.screens.AudioListScreen
import com.nuclearcode.teyesmusicplayer.ui.AudioPlayerViewModel
import com.nuclearcode.teyesmusicplayer.ui.screens.FavoritesScreen
import com.nuclearcode.teyesmusicplayer.utility.PermissionHandler

@Composable
fun AppNavHost(
    audioViewModel: AudioPlayerViewModel,
    navHostController: NavHostController,
    startDestination: String,
) {
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
                    navHostController = navHostController,
                    startDestination = startDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = if (isExpandedHost) {
                Modifier // без паддинга
            } else {
                Modifier.padding(innerPadding)
            },
            navController = navHostController,
            startDestination = startDestination,
        ) {
            composable("play_list") {
                AudioListScreen(
                    viewModel = audioViewModel,
                    isExpandedHost = isExpandedHost,
                    onExpandChangeHost = { isExpandedHost = it },
                )
            }
            composable("favorites") { FavoritesScreen() }
            composable("search_place") { DirectorySelector(audioViewModel) }
        }
    }
}