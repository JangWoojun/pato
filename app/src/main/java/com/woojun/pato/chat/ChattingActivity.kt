package com.woojun.pato.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.woojun.pato.BuildConfig
import com.woojun.pato.R
import com.woojun.pato.auth.AppPreferences
import com.woojun.pato.databinding.ActivityChattingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChattingBinding
    private val chatList = mutableListOf<Chat>()
    private val adapter = ChatAdapter(chatList)
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${BuildConfig.baseUrl}chat")
            .header("Authorization", "Bearer ${AppPreferences.token}")
            .build()

        val gson = Gson()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                CoroutineScope(Dispatchers.Main).launch {
                    val chat = gson.fromJson(text, ResponseChat::class.java)
                    Log.d("확인 문자열1", chat.toString())
                    Log.d("확인 문자열2", text)

                    if (chat.status) {
                        adapter.addChat(Chat(decodeBase64ToString(chat.data), false, convertISO8601ToTime(chat.time)))
                        binding.chatRecycler.scrollToPosition(adapter.getChat().size - 1)
                    } else {
                        Toast.makeText(this@ChattingActivity, "상대가 대화를 종료하셨습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                Log.d("확인", "시작")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                webSocket.close(1000, null)
            }
        }

        binding.chatRecycler.layoutManager = LinearLayoutManager(this@ChattingActivity)
        binding.chatRecycler.adapter = adapter

        webSocket = client.newWebSocket(request, listener)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.sendButton.setOnClickListener {
            val chat = gson.toJson(RequestChat(encodeStringToBase64(binding.input.text.toString()), "chat"))
            webSocket!!.send(chat)

            adapter.addChat(Chat(binding.input.text.toString(), true, getDate()))
            binding.chatRecycler.scrollToPosition(adapter.getChat().size - 1)
            binding.input.text.clear()
            Log.d("확인 채팅 보내기", chat.toString())
        }
    }

    private fun decodeBase64ToString(base64String: String): String {
        val decodedByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return String(decodedByteArray)
    }

    private fun encodeStringToBase64(stringToEncode: String): String {
        val encodedByteArray = Base64.encode(stringToEncode.toByteArray(), Base64.DEFAULT)
        return String(encodedByteArray)
    }

    private fun convertISO8601ToTime(iso8601String: String): String {
        val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()

        try {
            val date = iso8601Format.parse(iso8601String)
            if (date != null) {
                calendar.time = date
                calendar.add(Calendar.HOUR_OF_DAY, -3)

                val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val amPm = if (hourOfDay < 12) "오전" else "오후"
                val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12

                return "$amPm $hour:${String.format("%02d", minute)}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
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

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, null)
    }
}