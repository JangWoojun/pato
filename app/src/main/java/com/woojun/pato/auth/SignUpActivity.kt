package com.woojun.pato.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}