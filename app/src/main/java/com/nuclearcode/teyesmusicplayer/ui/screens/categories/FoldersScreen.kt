package com.nuclearcode.teyesmusicplayer.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.R
import com.nuclearcode.teyesmusicplayer.ui.AudioPlayerViewModel

@UnstableApi
@Composable
fun FoldersScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel,
    onClick: () -> Unit,
) {
    val dirs by viewModel.directories.collectAsState()

    Box(modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.rounded_arrow_back_24),
            contentDescription = null
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(dirs) {
                Text(
                    it,
                    modifier = Modifier
                        .background(Color.Cyan)
                        .clickable {
                            onClick()
                        })
            }
        } // lazyC end
    } //main container end
}