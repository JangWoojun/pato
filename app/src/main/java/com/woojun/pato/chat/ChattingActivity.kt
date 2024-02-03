package com.woojun.pato.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityChattingBinding

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChattingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor
    }
}