package com.kumar.seekhoanimeapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String? = null,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {

    object Main : Screen("main_screen")
    object Detail : Screen("detail_screen")

    object Explore : Screen(
        "explore_screen",
        "Explore",
        Icons.Filled.Explore,
        Icons.Outlined.Explore
    )

    object Favorites : Screen(
        "favorites_screen",
        "Favorites",
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder
    )

    object About : Screen(
        "about_screen",
        "About",
        Icons.Filled.Info,
        Icons.Outlined.Info
    )
}
