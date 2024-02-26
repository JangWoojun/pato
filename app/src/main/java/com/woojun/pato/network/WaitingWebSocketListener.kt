package com.woojun.pato.network

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.woojun.pato.chat.MatchingWaiting
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WaitingWebSocketListener(val context: Context, val callBack: () -> Unit) : WebSocketListener() {
    private val gson = Gson()

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, MatchingWaiting::class.java)
        if (message.status) {
            callBack()
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        Toast.makeText(context, "매칭에 실패하였습니다", Toast.LENGTH_SHORT).show()
    }
}