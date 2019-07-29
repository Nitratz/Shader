package com.nitratz.shader.model.request

import com.google.gson.annotations.SerializedName

data class PhoneNumberOTP(@SerializedName("phone_number") val phoneNumber: String)