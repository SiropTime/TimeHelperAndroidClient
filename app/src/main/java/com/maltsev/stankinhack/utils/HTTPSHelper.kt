package com.maltsev.stankinhack.utils

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.GsonBuilder
import com.maltsev.stankinhack.BOT_SENDER
import com.maltsev.stankinhack.postsService
import com.maltsev.stankinhack.screens.messagesList
import com.maltsev.stankinhack.screens.postsList
import com.maltsev.stankinhack.screens.speech
import com.maltsev.stankinhack.speechService
import com.maltsev.stankinhack.ui.items.swapList
import com.maltsev.stankinhack.utils.model.Message
import com.maltsev.stankinhack.utils.model.Post
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import java.util.*

val SPEECH_URL = "http://192.168.8.133:8081"
val POSTS_URL = "http://192.168.8.133:8080"


interface SpeechService {
    @POST("/message")
    suspend fun sendMessage(@Body requestBody: RequestBody): Response<Message>

    @Multipart
    @POST("/listen")
    suspend fun sendAudio(@Part requestBody: RequestBody, @Part file: MultipartBody.Part): Response<Message>
}

interface PostsService {
    @POST("/posts/user/{user_id}/post")
    suspend fun createPost(@Path(value="user_id") user_id: Int, @Body requestBody: RequestBody)

    @PUT("/posts/user/{user_id}/post/{id}")
    suspend fun editPost(@Path(value="user_id") user_id: Int, @Path(value = "id") id: Int, @Body requestBody: RequestBody)

    @GET("/posts/{id}")
    suspend fun getPostById(@Path(value="id") id: Int): Response<Post>

    @GET("/posts/all")
    suspend fun getPostAll(): Response<List<Post>>
}

suspend fun makeSendingAudio(file: File) {
    val client = OkHttpClient()
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("file", "audio", RequestBody.create("audio/x-wav".toMediaTypeOrNull(), file))
        .build()
    val request = Request.Builder()
        .url("http://192.168.8.133:8080/listen")
        .post(requestBody).build()
    var response = client.newCall(request).execute()
    val gson = GsonBuilder().setPrettyPrinting().create()
    val messageBody = response.body!!.string()
    val messageText = gson.fromJson(messageBody, Message::class.java)
        .text?: "Извини, не смог понять, что тебе нужно :( Но я отправил коллегам на рассмотрение!"
    val messageResponse = Message(sender= BOT_SENDER, text=messageText)


    Log.d("SEND_AUDIO", response.message)
    val job = GlobalScope.launch(Dispatchers.IO) {
        val tempList = mutableStateListOf<Message>()
        tempList.swapList(messagesList)
        messagesList.add(messageResponse)
        tempList.swapList(messagesList)
        speech!!.speakOut(messageResponse.text)
        Log.d("MESSAGES", messagesList.toString())
    }
    job.join()
}

fun makeGetPostAll(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = postsService.getPostAll()

        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                val postsBody = response.body()?.asReversed()
                Log.d("POSTS_BODY", postsBody.toString())
                val job = GlobalScope.launch(Dispatchers.IO) {
                    postsBody?.forEach { post ->
                        postsList.add(post)
                    }
                }
                job.join()
            } else {
                Log.e("BLOG", "Something went wrong")
                Looper.prepare()
                var errorBody = ""
                response.errorBody()?.let{errorBody = it.string()}
                Log.e("ERROR", errorBody)
                Toast.makeText(context,
                    "Проблемы с интернетом! Проверьте разрешения или подключение",
                    Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }
    }
}

fun makeSendingMessage(message: String, context: Context) {
    val jsonObject = JSONObject()
    jsonObject.put("text", message)

    val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    Log.d("REQUEST", jsonObject.toString())
    CoroutineScope(Dispatchers.IO).launch {
        val response = speechService.sendMessage(requestBody)

        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                if (response.code() == 200) {
                    val messageBody = response.body()
                    Log.d("MESSAGE_BODY", messageBody.toString())
                    var messageText = messageBody?.text?: "N/A"
                    if (messageText == "" || messageText == "N/A") {
                        messageText = "Извини, не смог понять, что тебе нужно :( Но я отправил коллегам на рассмотрение!"
                    }
                    Log.d("MESSAGE_TEXT", messageText)
                    val messageSender = BOT_SENDER
                    val messageResponse = Message(messageSender, messageText)
                    val job = GlobalScope.launch(Dispatchers.IO) {
                        val tempList = mutableStateListOf<Message>()
                        tempList.swapList(messagesList)
                        messagesList.add(messageResponse)
                        tempList.swapList(messagesList)
                        Log.d("MESSAGES", messagesList.toString())
                    }
                    job.join()
                } else if (response.code() == 202) {
                    val messageText = "Извини, я не понял, что ты имеешь ввиду"
                    val messageSender = BOT_SENDER
                    val messageResponse = Message(messageSender, messageText)
                    val job = GlobalScope.launch(Dispatchers.IO) {
                        val tempList = mutableStateListOf<Message>()
                        tempList.swapList(messagesList)
                        messagesList.add(messageResponse)
                        tempList.swapList(messagesList)
                    }
                    job.join()
                }

            } else {
                Log.e("HTTP", "Failed sending message")
                var errorBody = ""
                response.errorBody()?.let{errorBody = it.string()}
                Log.e("ERROR", errorBody)
            }
        }
    }
}
