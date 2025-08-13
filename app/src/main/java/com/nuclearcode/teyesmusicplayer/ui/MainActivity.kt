package com.nuclearcode.teyesmusicplayer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.nuclearcode.teyesmusicplayer.PlayerApp
import com.nuclearcode.teyesmusicplayer.ui.navigation.AppNavHost
import com.nuclearcode.teyesmusicplayer.ui.theme.TeyesMusicPlayerTheme
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
        requestPermissionIfNeeded()

        enableEdgeToEdge()
        setContent {
            TeyesMusicPlayerTheme {
                val navController = rememberNavController()

                AppNavHost(
                    viewModel,
                    navController,
                    "play_list"
                )
            }
        }
    }
    private fun requestPermissionIfNeeded() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 123)
        }
    }
}