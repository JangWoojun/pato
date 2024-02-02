package com.woojun.pato.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}