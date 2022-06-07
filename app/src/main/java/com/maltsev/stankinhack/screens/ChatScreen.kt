package com.maltsev.stankinhack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.maltsev.stankinhack.BOT_SENDER
import com.maltsev.stankinhack.ui.items.ChatUI
import com.maltsev.stankinhack.ui.items.SendMessageUI
import com.maltsev.stankinhack.utils.model.Message
import com.maltsev.stankinhack.utils.SpeechClass

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4)
@Composable
fun ChatScreenPreview() {
    ChatScreen(navController = rememberNavController())
}

val messageFieldText = mutableStateOf("")

val canTalking = mutableStateOf(true)

val messagesList = mutableStateListOf<Message>(Message(BOT_SENDER, "Привет, пользователь!"), Message(BOT_SENDER, "Спрашивай, что интересно!"))

var speech: SpeechClass? = null

@Composable
fun ChatScreen(navController: NavController) {
    val context = LocalContext.current
    speech = SpeechClass(context)

            Surface {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween

                ) {

                    // Chat - ChatUI.kt
                    Row(
                        modifier = Modifier
                            .weight(0.75f)
                    ) {
                        ChatUI()
                    }

                    Divider(
                        color = MaterialTheme.colors.primary,
                        thickness = 2.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    // Row with message field, voice button and send button
                    Row(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ) {
                        SendMessageUI()
                    }

                }
            }


}