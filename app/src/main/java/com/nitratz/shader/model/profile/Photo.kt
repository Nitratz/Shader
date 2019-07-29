package com.nitratz.shader.model.profile


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String
)