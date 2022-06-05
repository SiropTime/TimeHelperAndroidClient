package com.maltsev.stankinhack.screens

sealed class Screen(val route: String) {
    object Chat: Screen(route = "main_screen")
    object Intro: Screen(route = "intro_screen")
    object Home: Screen(route = "home_screen")
    object Tasks: Screen(route = "tasks_screen")
}