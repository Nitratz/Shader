package com.nitratz.shader.model.validation


import com.google.gson.annotations.SerializedName
import com.nitratz.shader.model.Meta

data class OtpResponse(
        @SerializedName("data")
    val data: OtpData,
        @SerializedName("meta")
    val meta: Meta
)


