package com.maltsev.stankinhack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.GsonBuilder
import com.maltsev.stankinhack.ui.items.BottomBar
import com.maltsev.stankinhack.ui.items.BottomTabs
import com.maltsev.stankinhack.ui.theme.StankinHackTheme
import com.maltsev.stankinhack.utils.SetupNavGraph
import com.maltsev.stankinhack.utils.SpeechService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BOT_SENDER = "bot"

val builder = GsonBuilder()
val gson = builder.setLenient().create()
//var recorder: MediaRecorder? = null


val retrofit : Retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.8.133:8081")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

 val speechService: SpeechService = retrofit.create(SpeechService::class.java)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var navController: NavHostController

        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val tabs = remember { BottomTabs.values() }
            val nav = rememberNavController()
            StankinHackTheme {
                Scaffold(
                    topBar = { TopAppBar (
                        title = { Text ("TimeHelper", color = Color.White, textAlign = TextAlign.Center)
                        }, backgroundColor =  MaterialTheme.colors.primary) },

                    bottomBar = {
                        BottomBar(navController = nav, tabs = tabs)
                    }
                ) { innerPaddingModifier ->
                    navController = rememberNavController()
                    SetupNavGraph(
                        navController = nav,
                        modifier = Modifier.padding(innerPaddingModifier)
                    )
                }
            }
        }
    }
}

