package com.example.mystoryapps.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mystoryapps.R
import com.example.mystoryapps.utils.UserPreference

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val userPreference = UserPreference(this)
        val token = userPreference.getToken()
        return token.isNotEmpty()
    }
}