package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SelectionBottomBar(
    selectedCount: Int,
    onBatchAction: (BatchAction) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { onBatchAction(BatchAction.AddToQueue) },
            icon = { Icon(Icons.Default.PlayArrow, null) },
            label = { Text("В очередь ($selectedCount)") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onBatchAction(BatchAction.AddToPlaylist) },
            icon = { Icon(Icons.Default.Add, null) },
            label = { Text("В плейлист") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onBatchAction(BatchAction.Pin) },
            icon = { Icon(Icons.Default.ShoppingCart, null) },
            label = { Text("Закрепить") }
        )
    }
}
