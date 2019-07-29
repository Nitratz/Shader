package com.nitratz.shader.model.profile


import com.google.gson.annotations.SerializedName
import com.nitratz.shader.model.Meta

data class ProfilesResponse(
        @SerializedName("data")
    val data: Data,
        @SerializedName("meta")
    val meta: Meta
)
