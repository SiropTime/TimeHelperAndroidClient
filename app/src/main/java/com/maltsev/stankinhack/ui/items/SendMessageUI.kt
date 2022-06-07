package com.maltsev.stankinhack.ui.items

import android.media.MediaRecorder
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.maltsev.stankinhack.screens.canTalking
import com.maltsev.stankinhack.screens.messageFieldText
import com.maltsev.stankinhack.screens.messagesList
import com.maltsev.stankinhack.utils.model.Message
import com.maltsev.stankinhack.utils.makeSendingAudio
import com.maltsev.stankinhack.utils.makeSendingMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

var audioRecorder: MediaRecorder? = null
val isListening = mutableStateOf(false)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SendMessageUI() {
    val tempList = remember { mutableStateListOf<Message>() }

    val keyboardController = LocalSoftwareKeyboardController.current
    val isPressed = remember { mutableStateOf(false) }

    tempList.swapList(messagesList)
    val context = LocalContext.current
    val path = context.filesDir
    val audioDirectory =  File(path, "audio")
    audioDirectory.mkdirs()
    val file = File(audioDirectory, "voice.wav")
        Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 5.dp)
                .height(50.dp)
                .widthIn(max = 240.dp)
                .clip(CircleShape)
                .border(
                    color = MaterialTheme.colors.primary,
                    width = 2.dp,
                    shape = CircleShape
                ),
            value = messageFieldText.value,
            placeholder = { Text(text = "Введите сообщение") } ,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    makeSendingMessage(messageFieldText.value, context)
                    canTalking.value = true
                    messagesList.add(Message("man", messageFieldText.value))
                    tempList.swapList(messagesList)
                    messageFieldText.value = ""
                }
            ),
            onValueChange = {
                canTalking.value = it == ""
                messageFieldText.value = it
            })

        if (canTalking.value) {
            // Voice button
            OutlinedButton(
                onClick = {
                    if (isAudioRecording()) {
                        stopRecording()
                        isPressed.value = false
                        CoroutineScope(Dispatchers.IO).launch {
                            makeSendingAudio(file)
                        }
                    } else {
                        file.createNewFile()
                        startRecording(file)
                        isPressed.value = true
                    }
                    },
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 4.dp)
                    .size(50.dp)
                    .border(
                        color = MaterialTheme.colors.primary,
                        width = 2.dp,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    contentColor = if (isPressed.value) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground,
                    backgroundColor = if (isPressed.value) MaterialTheme.colors.primary else Color.White
                ),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape,
            ) {
                Icon(Icons.Outlined.Mic, "Talk")
            }
        } else {
            // Send Button
            OutlinedButton(
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 4.dp)
                    .size(50.dp)
                    .border(
                        color = MaterialTheme.colors.primary,
                        width = 2.dp,
                        shape = CircleShape
                    ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    makeSendingMessage(messageFieldText.value, context)
                    canTalking.value = true
                    messagesList.add(Message("man", messageFieldText.value))
                    tempList.swapList(messagesList)
                    messageFieldText.value = ""
                }
            ) {
                Icon(Icons.Outlined.Send, "Send")
            }
        }
    }
}

@Suppress("DEPRECATION")
fun startRecording(file: File) {
    Log.d("AUDIO", "Starting recording")
    try {
        audioRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file)
            prepare()
            start()
        }
    } catch (e: Exception) {

    }

}

fun stopRecording() {
    audioRecorder?.let {
        Log.d("AUDIO", "Stopping recording")
        it.stop()
        it.release()
    }
    audioRecorder = null
}

fun isAudioRecording() = audioRecorder != null