package com.nitratz.shader.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.nitratz.shader.R
import com.nitratz.shader.RestClient
import com.nitratz.shader.model.TinderEndpoints
import com.nitratz.shader.model.request.AuthenticateRefreshToken
import com.nitratz.shader.model.request.ValidateOtpForPhone
import com.nitratz.shader.model.validation.OtpData
import com.nitratz.shader.model.validation.toOtpData
import com.nitratz.shader.model.validation.toTokenData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response




class PinActivity : AppCompatActivity() {

    private lateinit var mShared: SharedPreferences
    private lateinit var mLayout: View
    private lateinit var mBulletsImageViews: List<ImageView>
    private lateinit var mOtpCodeEditText: EditText

    private lateinit var mPhoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        mShared = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE)
        mLayout = findViewById(R.id.pin_layout)
        mOtpCodeEditText = findViewById(R.id.edit_otp)
        mBulletsImageViews = arrayListOf(
            findViewById(R.id.bullet_1), findViewById(R.id.bullet_2), findViewById(
                R.id.bullet_3
            ),
            findViewById(R.id.bullet_4), findViewById(R.id.bullet_5), findViewById(
                R.id.bullet_6
            )
        )

        mLayout.setOnClickListener {
            mOtpCodeEditText.focusWithKeyboard()
        }

        mPhoneNumber = mShared.getString(getString(R.string.key_phone), "")!!
        mOtpCodeEditText.focusWithKeyboard()
        setOtpListener()
    }

    private fun setOtpListener() {
        mOtpCodeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(otp: CharSequence?, start: Int, count: Int, after: Int) {
                val bullet = mBulletsImageViews[start]

                when (bullet.visibility) {
                    View.INVISIBLE -> bullet.visibility = View.VISIBLE
                    View.VISIBLE -> bullet.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(otp: CharSequence?, start: Int, count: Int, after: Int) {
                if (start + 1 == mBulletsImageViews.size) {
                    mOtpCodeEditText.hideKeyBoard()
                    handleOtpVerification(otp.toString())
                }
            }
        })
    }

    private fun handleOtpVerification(otpCode: String) {
        GlobalScope.async(Dispatchers.IO) {
            val otpResponse = sendVerificationOtpCode(otpCode)
            if (otpResponse.isSuccessful) {
                val otpData = otpResponse.body?.string()?.toOtpData()
                val tokenResponse = authenticateWithRefreshToken(otpData!!)
                storeTokensAndStartActivity(tokenResponse)
            }
        }
    }

    private fun sendVerificationOtpCode(otpCode: String): Response {
        val body: RequestBody = Gson()
            .toJson(ValidateOtpForPhone(otpCode, mPhoneNumber))
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(TinderEndpoints.VALIDATE_OTP_SMS)
            .post(body)
            .build()

        return RestClient.instance.client.newCall(request).execute()
    }

    private fun authenticateWithRefreshToken(otpResponse: OtpData): Response {
        val body: RequestBody = Gson()
            .toJson(AuthenticateRefreshToken(otpResponse.refreshToken, mPhoneNumber))
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(TinderEndpoints.LOGIN_SMS)
            .post(body)
            .build()

        return RestClient.instance.client.newCall(request).execute()
    }

    private fun storeTokensAndStartActivity(tokenResponse: Response) {
        if (tokenResponse.isSuccessful) {
            val tokenData = tokenResponse.body?.string()?.toTokenData()!!

            mShared.edit()
                .putString(getString(R.string.key_user_id), tokenData.id)
                .putString(getString(R.string.key_token), tokenData.apiToken)
                .putString(getString(R.string.key_refresh_token), tokenData.refreshToken)
                .apply()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this@PinActivity, tokenResponse.message, Toast.LENGTH_LONG).show()
        }
    }


    fun EditText.focusWithKeyboard() {
        mOtpCodeEditText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

    }

    fun EditText.hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0)
    }
}