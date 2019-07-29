package com.nitratz.shader.model.profile

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("results")
        val results: List<UserResult>
)
