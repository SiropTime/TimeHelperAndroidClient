package com.maltsev.stankinhack.utils.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("sender")
    val sender: String,
    @SerializedName("text")
    val text: String
)