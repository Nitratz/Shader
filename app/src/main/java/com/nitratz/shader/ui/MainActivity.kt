package com.nitratz.shader.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bartoszlipinski.flippablestackview.FlippableStackView
import com.google.gson.Gson
import com.nitratz.shader.R
import com.nitratz.shader.adapter.ProfileAdapter
import com.nitratz.shader.model.profile.ProfilesResponse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val json = this.resources.openRawResource(R.raw.test).bufferedReader().use { it.readText() }

        val tinder = Gson().fromJson(json, ProfilesResponse::class.java)
        val stack: FlippableStackView = findViewById(R.id.stack)
        stack.initStack(tinder.data.results.size)
        stack.adapter = ProfileAdapter(this, tinder.data.results)
    }
}
