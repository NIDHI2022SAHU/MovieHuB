package com.example.moviehub.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.moviehub.R
import com.example.moviehub.utilities.Constants.Companion.IS_LOGGED_IN
import com.example.moviehub.utilities.SharedPreferencesHelper

class SplashActivity : AppCompatActivity() {
    private lateinit var pref: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        pref = SharedPreferencesHelper
        pref.initSharedPreferences(applicationContext)
        Handler(Looper.getMainLooper()).postDelayed({
            val isLoggedIn = pref.getValue(IS_LOGGED_IN, false)
            if (isLoggedIn as Boolean) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        }, 2000)
    }
}