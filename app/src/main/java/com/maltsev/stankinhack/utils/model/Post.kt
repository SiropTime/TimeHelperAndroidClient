package com.maltsev.stankinhack.utils.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val label: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("date")
    val date: String
)