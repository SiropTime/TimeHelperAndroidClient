package com.maltsev.stankinhack.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maltsev.stankinhack.screens.ChatScreen
import com.maltsev.stankinhack.screens.HomeScreen
import com.maltsev.stankinhack.screens.IntroScreen
import com.maltsev.stankinhack.screens.Screen

@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(

        navController = navController,
        startDestination = Screen.Intro.route
    ) {
        composable(
            route = Screen.Intro.route
        ) {
            Box(modifier = modifier) {
                IntroScreen(navController = navController)
            }

        }
        composable(
            route = Screen.Chat.route
        ) {
            Box(modifier = modifier) {
                ChatScreen(navController = navController)
            }

        }
        composable(
            route = Screen.Home.route
        ) {
            Box(modifier = modifier) {
                HomeScreen(navController = navController)
            }

        }
        composable(
            route = Screen.Tasks.route
        ) {
            Box(modifier = modifier) {}

        }
    }
}