package com.nuclearcode.teyesmusicplayer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nuclearcode.teyesmusicplayer.PlayerApp
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val tracks by viewModel.audioFiles.collectAsState() //todo вытащить все действия и данные к сюда чтобы не передавать всю вьюмодель

                    AudioListScreen(
                        modifier = Modifier.padding(innerPadding), viewModel
                    )

                }
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