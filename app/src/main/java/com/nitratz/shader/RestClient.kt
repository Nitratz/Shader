package com.nitratz.shader

import android.text.TextUtils
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import okhttp3.OkHttpClient

class RestClient private constructor() {
    private val okHttpClient: OkHttpClient = OkHttpClient()



    companion object {
        val USER_AGENT =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36"

        private val INSTANCE: RestClient by lazy {
            RestClient()
        }

        val instance: RestClient
            get() {
                return INSTANCE
            }

        fun getUrl(url: String): GlideUrl? {
            return if (TextUtils.isEmpty(url)) {
                null
            } else
                GlideUrl(url, LazyHeaders.Builder()
                        .addHeader("User-Agent", USER_AGENT)
                        .build())
        }
    }

    val client: OkHttpClient
        get() {
            return okHttpClient
        }
}
