package com.kumar.seekhoanimeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kumar.seekhoanimeapp.ui.screens.detail.AnimeDetailScreen
import com.kumar.seekhoanimeapp.ui.screens.mainscreen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route + "/{animeId}",
            arguments = listOf(
                navArgument("animeId") {
                    type = NavType.IntType
                }
            )
        ) {
            AnimeDetailScreen(navController = navController)
        }
    }
}