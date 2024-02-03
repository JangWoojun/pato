package com.woojun.pato.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window.statusBarColor = statusBarColor

        // TODO 필수 필드를 만족해야 이동하도록 수정

        binding.finishButton.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finishAffinity()
        }
    }
}