package com.nitratz.shader.model.request

import com.google.gson.annotations.SerializedName

data class AuthenticateRefreshToken(@SerializedName("refresh_token") val refreshToken: String,
                                    @SerializedName("phone_number") val phoneNumber: String)