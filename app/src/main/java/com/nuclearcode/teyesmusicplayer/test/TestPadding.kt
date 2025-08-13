package com.nuclearcode.teyesmusicplayer.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true, backgroundColor = 0x0000000000, showSystemUi = true)
fun TestPadding() {

    Box(
        Modifier
            .padding(100.dp)
            .background(Color.Black)
            .size(100.dp)
            .padding(12.dp)
    ) {
        Text("123", color = Color.White)
    }
}