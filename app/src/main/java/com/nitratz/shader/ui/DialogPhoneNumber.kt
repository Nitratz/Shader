package com.nitratz.shader.ui

import android.app.Dialog
import android.os.Bundle
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.nitratz.shader.R


class DialogPhoneNumber(private val mContext: Context) : Dialog(mContext), View.OnClickListener {

    private val mShared = mContext.getSharedPreferences(mContext.getString(R.string.shared_prefs), Context.MODE_PRIVATE)
    private lateinit var mEditNumber: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.dialog_phone_number)

        findViewById<Button>(R.id.save).setOnClickListener(this)
        mEditNumber = findViewById(R.id.phone_number)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.save -> {
                val phoneNumber = getPhoneNumber()

                if (phoneNumber.isNotEmpty()) {
                    mShared.edit()
                            .putString(mContext.getString(R.string.key_phone), getPhoneNumber())
                            .apply()
                    dismiss()
                }
            }
        }
    }

    private fun getPhoneNumber(): String {
        val phone = mEditNumber.text.toString()

        if (phone.isEmpty() || phone.length > 12 || !phone.contains('+')) {
            Toast.makeText(mContext, R.string.valid_phone_number, Toast.LENGTH_LONG).show()
            return ""
        }

        return phone.split('+')[1]
    }
}