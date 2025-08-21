package com.nuclearcode.teyesmusicplayer.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.nuclearcode.teyesmusicplayer.PlayerApp
import com.nuclearcode.teyesmusicplayer.test.AudioTestService
import com.nuclearcode.teyesmusicplayer.test.TestForegroundService
import com.nuclearcode.teyesmusicplayer.ui.navigation.AppNavHost
import com.nuclearcode.teyesmusicplayer.ui.theme.TeyesMusicPlayerTheme
import com.nuclearcode.teyesmusicplayer.utility.AudioPlaybackService
import javax.inject.Inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: AudioPlayerViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AudioPlayerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as PlayerApp).appComponent.injectMainActivity(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            } else {
                startForegroundService(Intent(this, TestForegroundService::class.java))
            }
        } else {
            startService(Intent(this, TestForegroundService::class.java))
        }

//        enableEdgeToEdge()
//        setContent {
//            TeyesMusicPlayerTheme {
//                val navController = rememberNavController()
//
//                AppNavHost(
//                    viewModel,
//                    navController,
//                    "play_list"
//                )
//            }
//        }
    }
}