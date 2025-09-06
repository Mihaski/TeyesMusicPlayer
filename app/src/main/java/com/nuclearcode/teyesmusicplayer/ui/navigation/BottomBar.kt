package com.nuclearcode.teyesmusicplayer.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavBackStack
import com.nuclearcode.teyesmusicplayer.ui.CategoryHolder
import com.nuclearcode.teyesmusicplayer.ui.theme.Pink40

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    backStack: NavBackStack,
) {
    val current = backStack.lastOrNull() ?: NavigationItems.PlayList

    NavigationBar(
        modifier = modifier,
        containerColor = Pink40
    ) {
        listOf(
            NavigationItems.MainScreen(CategoryHolder.AllCategories),
            NavigationItems.PlayList,
            NavigationItems.ReadsFields,
            NavigationItems.Favorites,
            NavigationItems.SearchPlaces
        ).forEach { screen ->
            NavigationBarItem(
                selected = current::class == screen::class,
                onClick = {
                    // заменяем весь стек новым экраном (чтобы вкладки не наслаивались)
                    backStack.clear()
                    backStack.add(screen)
                },
                icon = {
                    Icon(
                        painterResource(screen.iconRes),
                        contentDescription = screen.label
                    )
                },
                label = { Text(screen.label) },
                colors = NavigationBarItemDefaults.colors(
//todo цвета поменять
                )
            )
        }
    }
}