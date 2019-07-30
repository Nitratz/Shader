package com.nitratz.shader.model.validation


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.nitratz.shader.model.TinderResponse
import java.lang.reflect.Type

data class TokenData(
    @SerializedName("api_token")
    val apiToken: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("is_new_user")
    val isNewUser: Boolean,
    @SerializedName("refresh_token")
    val refreshToken: String
)

fun String.toTokenData(): TokenData {
    val type: Type = object : TypeToken<TinderResponse<TokenData>>() {}.type
    val resp: TinderResponse<TokenData> = Gson().fromJson(this, type)

    return resp.data
}