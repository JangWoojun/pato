package com.woojun.pato

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.woojun.pato.auth.AppPreferences
import com.woojun.pato.auth.LoginActivity
import com.woojun.pato.auth.ProfileActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.statusBarColor = statusBarColor

        Handler(Looper.getMainLooper()).postDelayed({
            if (AppPreferences.profile && AppPreferences.token != "") {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else if (!AppPreferences.profile) {
                startActivity(Intent(this@SplashActivity, ProfileActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }, 1500)
    }
}