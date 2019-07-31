package com.nitratz.shader.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bartoszlipinski.flippablestackview.FlippableStackView
import com.nitratz.shader.R
import com.nitratz.shader.RestClient
import com.nitratz.shader.adapter.ProfileAdapter
import com.nitratz.shader.model.TinderEndpoints
import com.nitratz.shader.model.profile.toUserData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Request
import okhttp3.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mStackView: FlippableStackView

    private lateinit var mShared: SharedPreferences
    private var mApiToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mShared = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE)
        mApiToken = mShared.getString(getString(R.string.key_token), "")

        mStackView = findViewById(R.id.stack)

        getTinderProfiles()
    }

    private fun getTinderProfiles() {
        GlobalScope.async {
            val teasersResp = callTinderTeasers()

            if (teasersResp.isSuccessful) {
                val userData = teasersResp.body?.string()?.toUserData()!!
                runOnUiThread {
                    mStackView.apply {
                        initStack(1)
                        adapter = ProfileAdapter(this@MainActivity, userData.results)
                    }
                }
            }
        }
    }

    private fun callTinderTeasers(): Response {
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Auth-Token", mApiToken!!)
            .url(TinderEndpoints.TINDER_TEASERS)
            .get()
            .build()

        return RestClient.instance.client.newCall(request).execute()
    }
}