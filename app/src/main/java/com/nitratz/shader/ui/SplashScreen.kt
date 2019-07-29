package com.nitratz.shader.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.view.animation.LinearInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.gson.Gson
import com.nitratz.shader.R
import com.nitratz.shader.RestClient
import com.nitratz.shader.model.TinderEndpoints
import com.nitratz.shader.model.request.PhoneNumberOTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class SplashScreen : AppCompatActivity() {
    private lateinit var mShared: SharedPreferences
    private lateinit var dialogPhoneNumber: DialogPhoneNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupAnimation()

        mShared = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE)
        dialogPhoneNumber = DialogPhoneNumber(this)

        dialogPhoneNumber.setOnDismissListener {
            val phoneNumber = mShared.getString(getString(R.string.key_phone), "")
            GlobalScope.async(Dispatchers.IO) {
                if (getTinderOtpCode(phoneNumber!!)) {
                    runOnUiThread {
                        startActivity(Intent(baseContext, PinActivity::class.java))
                        finish()
                    }
                }
            }
        }

        dialogPhoneNumber.show()
    }

    private fun setupAnimation() {
        val anim = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.42f)
        anim.duration = 1000
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE

        findViewById<ImageView>(R.id.logo_loading).startAnimation(anim)
    }

    private fun getTinderOtpCode(phoneNumber: String): Boolean {
        val body: RequestBody = Gson()
                .toJson(PhoneNumberOTP(phoneNumber))
                .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(TinderEndpoints.SEND_OTP_SMS)
                .post(body)
                .build()

        return true
    }
}