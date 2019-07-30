package com.nitratz.shader.model.profile

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.nitratz.shader.model.TinderResponse
import java.lang.reflect.Type

data class UserData(
    @SerializedName("results")
    val results: List<UserResult>
)

fun String.toUserData(): UserData {
    val type: Type = object : TypeToken<TinderResponse<UserData>>() {}.type
    val resp: TinderResponse<UserData> = Gson().fromJson(this, type)

    return resp.data
}
