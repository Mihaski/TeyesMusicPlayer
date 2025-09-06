package com.nuclearcode.teyesmusicplayer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.PlayerApp
import com.nuclearcode.teyesmusicplayer.ui.navigation.AppNavHost
import com.nuclearcode.teyesmusicplayer.ui.theme.TeyesMusicPlayerTheme
import javax.inject.Inject
import kotlin.getValue

@UnstableApi
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: AudioPlayerViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AudioPlayerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as PlayerApp).appComponent.injectMainActivity(this)

        enableEdgeToEdge()
        setContent {
            TeyesMusicPlayerTheme {

                AppNavHost(
                    viewModel
                )
            }
        }
    }
}