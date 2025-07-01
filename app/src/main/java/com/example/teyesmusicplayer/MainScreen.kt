package com.example.teyesmusicplayer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<MainViewModel>()
    MainView(modifier, viewModel)
}

@Composable
fun MainView(
    modifier: Modifier,
    viewModel: MainViewModel,
) {

    val context = LocalContext.current

    var tracks by remember { mutableStateOf<List<AudioFile?>>(emptyList()) }
    LaunchedEffect(Unit) {
        tracks = getAudioFiles(context)
    }
    var currentTrack by remember { mutableStateOf<AudioFile?>(null) }
    val progress by viewModel.progress.collectAsState()
    val duration by viewModel.duration.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Music Player", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Найдено: ${tracks.size}")

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(0.9f)
                .background(Color.Red)
        ) {
            items(tracks) { track ->
                if (track != null) {
                    Text(
                        text = track.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.playTrack(context, track.path)
                                currentTrack = track

                                Toast.makeText(
                                    context,
                                    "currentTrack:$currentTrack.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            .padding(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentTrack != null && duration > 0) {
            Slider(
                value = progress.toFloat(),
                onValueChange = { newValue ->
                    viewModel.seekTo(newValue.toInt())
                },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${progress / 1000}s / ${duration / 1000}s",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        currentTrack?.let {
            Button(onClick = {
                viewModel.stopPlayback()
                currentTrack = null
            }) {
                Text("Stop ${it.title}")
            }
        }

    }
}