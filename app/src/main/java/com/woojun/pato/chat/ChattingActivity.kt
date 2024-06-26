package com.woojun.pato.chat

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.woojun.pato.BuildConfig
import com.woojun.pato.R
import com.woojun.pato.AppPreferences
import com.woojun.pato.chat.dataClass.Chat
import com.woojun.pato.chat.dataClass.RequestChat
import com.woojun.pato.chat.dataClass.ResponseChat
import com.woojun.pato.databinding.ActivityChattingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding

    private val chatList = mutableListOf<Chat>()
    private val adapter = ChattingAdapter(chatList)
    private var webSocket: WebSocket? = null

    private var profileImage = ""


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
                    if (chat.status && chat.type == "information") {
                        profileImage = chat.opponent!!.image
                        binding.nicknameText.text = chat.opponent.nickname
                    } else if (chat.status && chat.type == "recivedChat") {
                        adapter.addChat(Chat(profileImage, decodeBase64ToString(chat.data), false, convertISO8601ToTime(chat.time)))
                        binding.chatRecycler.scrollToPosition(adapter.getChat().size - 1)
                    } else {
                        Toast.makeText(this@ChattingActivity, "상대가 대화를 종료하셨습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
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
            if (binding.input.text.isNotEmpty()) {
                val chat = gson.toJson(RequestChat(encodeStringToBase64(binding.input.text.toString()), "chat"))
                webSocket!!.send(chat)

                adapter.addChat(Chat(profileImage, binding.input.text.toString(), true, getDate()))
                binding.chatRecycler.scrollToPosition(adapter.getChat().size - 1)
                binding.input.text.clear()
            }
        }

        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    binding.sendIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#FF5656"))
                } else{
                    binding.sendIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#C4C4C4"))
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        setKeyboardVisibilityListener(object : OnKeyboardVisibilityListener {
            override fun onVisibilityChanged(visible: Boolean) {
                if (visible) binding.chatRecycler.scrollToPosition(adapter.getChat().size - 1)
            }
        })
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

    interface OnKeyboardVisibilityListener {
        fun onVisibilityChanged(visible: Boolean)
    }

    private fun AppCompatActivity.setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val estimatedKeyboardDP = defaultKeyboardHeightDP + 48
            private val rect = Rect()

            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, estimatedKeyboardDP.toFloat(), parentView.resources.displayMetrics).toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight

                if (isShown == alreadyOpen) {
                    println("Ignoring global layout change...")
                    return
                }
                alreadyOpen = isShown
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }
}