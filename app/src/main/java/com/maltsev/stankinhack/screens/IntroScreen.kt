package com.maltsev.stankinhack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maltsev.stankinhack.R
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(1000)
        navController.navigate(route = Screen.Chat.route)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(50.dp)
                .fillMaxSize(),
            alignment = Alignment.Center
        )

    }
}