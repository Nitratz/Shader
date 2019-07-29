package com.nitratz.shader.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.nitratz.shader.R
import com.nitratz.shader.RestClient
import com.nitratz.shader.model.TinderEndpoints
import com.nitratz.shader.model.request.AuthenticateRefreshToken
import com.nitratz.shader.model.request.ValidateOtpForPhone
import com.nitratz.shader.model.validation.OtpData
import com.nitratz.shader.model.validation.OtpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
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
            focusWithKeyboard()
        }

        focusWithKeyboard()
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
                    handleOtpVerification(otp.toString())
                }
            }
        })
    }

    private fun handleOtpVerification(otpCode: String) {

        GlobalScope.async(Dispatchers.IO) {
            val resp = sendVerificationOtpCode(otpCode)
            val respObj = Gson().fromJson(resp.body.toString(), OtpResponse::class.java)
            if (resp.isSuccessful)
                authenticateWithRefreshTOken(respObj)
        }
    }

    private fun sendVerificationOtpCode(otpCode: String): Response {
        val phoneNumber = mShared.getString(getString(R.string.key_phone), "")
        val body : RequestBody = Gson()
                .toJson(ValidateOtpForPhone(otpCode, phoneNumber!!, false))
                .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(TinderEndpoints.VALIDATE_OTP_SMS)
                .post(body)
                .build()

        return RestClient.instance.client.newCall(request).execute()
    }

    private fun authenticateWithRefreshTOken(otpReponse: OtpResponse) {
        val phoneNumber = mShared.getString(getString(R.string.key_phone), "")


        /*val body : RequestBody = Gson()
                .toJson(AuthenticateRefreshToken(otpCode, phoneNumber!!, false))
                .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(TinderEndpoints.VALIDATE_OTP_SMS)
                .post(body)
                .build()

        RestClient.instance.client.newCall(request).execute()*/
    }

    private fun focusWithKeyboard() {
        mOtpCodeEditText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mOtpCodeEditText, InputMethodManager.SHOW_IMPLICIT)
    }
}