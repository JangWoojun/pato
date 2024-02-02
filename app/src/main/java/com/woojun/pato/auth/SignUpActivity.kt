package com.woojun.pato.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window.statusBarColor = statusBarColor

        // TODO 모든 필드를 만족해야 이동하도록 수정

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, ProfileActivity::class.java))
            finishAffinity()
        }
    }
}