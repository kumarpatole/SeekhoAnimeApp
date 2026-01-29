package com.kumar.seekhoanimeapp.ui.screens.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kumar.seekhoanimeapp.ui.navigation.Screen
import com.kumar.seekhoanimeapp.ui.screens.about.AboutScreen
import com.kumar.seekhoanimeapp.ui.screens.explore.ExploreScreen
import com.kumar.seekhoanimeapp.ui.screens.favorites.FavoritesScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()
    val bottomNavItems = listOf(Screen.Explore, Screen.Favorites, Screen.About)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->

                    val isSelected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon!!
                                else screen.unselectedIcon!!,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title!!) },
                        selected = isSelected,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Explore.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Explore.route) {
                ExploreScreen(navController = navController)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(navController = navController)
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
        }
    }
}
