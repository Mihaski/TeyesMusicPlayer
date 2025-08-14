package com.nuclearcode.teyesmusicplayer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxSize(),
        Alignment.Center,
    ) {
        Text(
            text = "Favorites Screen",
            fontSize = 30.sp
        )
    }
}