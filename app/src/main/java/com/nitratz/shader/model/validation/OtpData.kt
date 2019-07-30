package com.nitratz.shader.model.validation

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.nitratz.shader.model.TinderResponse
import com.nitratz.shader.model.profile.UserData
import java.lang.reflect.Type

data class OtpData(
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("validated")
    val validated: Boolean
)

fun String.toOtpData(): OtpData {
    val type: Type = object : TypeToken<TinderResponse<OtpData>>() {}.type
    val resp: TinderResponse<OtpData> = Gson().fromJson(this, type)

    return resp.data
}
