package com.nitratz.shader.model.validation

import com.google.gson.annotations.SerializedName

data class OtpData(
        @SerializedName("refresh_token")
        val refreshToken: String,
        @SerializedName("validated")
        val validated: Boolean
)