package com.nitratz.shader.model.profile


import com.google.gson.annotations.SerializedName
import com.nitratz.shader.model.profile.Photo

data class User(
    @SerializedName("_id")
    val id: String,
    @SerializedName("photos")
    val photos: List<Photo>
)