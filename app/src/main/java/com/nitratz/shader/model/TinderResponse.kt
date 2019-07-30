package com.nitratz.shader.model

import com.google.gson.annotations.SerializedName

data class TinderResponse<T>(
    @SerializedName("data")
    val data: T,
    @SerializedName("meta")
    val meta: Meta
)