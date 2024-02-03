package com.woojun.pato.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityChattingBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChattingBinding
    private val chatList = mutableListOf(Chat("안녕하세요!", false, getDate()))
    private val adapter = ChatAdapter(chatList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor

        binding.chatRecycler.layoutManager = LinearLayoutManager(this@ChattingActivity)
        binding.chatRecycler.adapter = adapter

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.sendButton.setOnClickListener {
            adapter.addChat(Chat(binding.input.text.toString(), true, getDate()))
            binding.chatRecycler.scrollToPosition(adapter.getChat().size - 1)
            binding.input.text.clear()
        }
    }

    private fun getDate(): String {
        try {
            val localDateTime = LocalDateTime.now()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            val formattedDateTime = localDateTime.format(dateTimeFormatter).toString()

            val timeString = formattedDateTime.substring(8, 12)
            var hour = timeString.substring(0, 2).toInt()
            val minute = timeString.substring(2, 4).toInt()

            val amPmText = if (hour >= 12) "오후 " else "오전 "
            if (hour > 12) hour -= 12

            return "$amPmText${String.format("%02d:%02d", hour, minute)}"
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("시간 변환 오류")
        }
    }
}