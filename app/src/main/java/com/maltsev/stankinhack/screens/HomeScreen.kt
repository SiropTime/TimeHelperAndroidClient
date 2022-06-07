package com.maltsev.stankinhack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maltsev.stankinhack.ui.items.BlogList
import com.maltsev.stankinhack.utils.makeGetPostAll
import com.maltsev.stankinhack.utils.model.Post

val postsList = mutableListOf<Post>()

@Composable
fun HomeScreen(navController: NavController) {

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            BlogList()
        }
    }

}

