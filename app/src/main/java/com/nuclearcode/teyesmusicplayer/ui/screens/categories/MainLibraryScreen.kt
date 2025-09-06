package com.nuclearcode.teyesmusicplayer.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuclearcode.teyesmusicplayer.ui.CategoryHolder
import com.nuclearcode.teyesmusicplayer.ui.parts.CategoryIcon

@Preview
@Composable
fun PreviewMainLibraryScreen() {
    MainLibraryScreen {}
}

@Composable
fun MainLibraryScreen(
    modifier: Modifier = Modifier,
    onClick: (CategoryHolder) -> Unit = {}
) {
    val categories = listOf(
        CategoryHolder.AllTracks,
        CategoryHolder.Folders,
        CategoryHolder.Favorites,
        CategoryHolder.Albums,
        CategoryHolder.Authors,
        CategoryHolder.Genres,
        CategoryHolder.Years,
    )

    LazyColumn(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(categories) { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(category)
                    },
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(
                    ImageVector.vectorResource(category.iconRes),
                    Color(category.colorValue)
                )
                Text(
                    category.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } // lazyC end
}