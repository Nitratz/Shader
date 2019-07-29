package com.nitratz.shader.model.request

import com.google.gson.annotations.SerializedName

data class ValidateOtpForPhone(@SerializedName("otp_code") val otp: String,
                               @SerializedName("phone_number") val phoneNumber: String,
                               @SerializedName("is_update") val update: Boolean)