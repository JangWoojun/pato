package com.woojun.pato.chat

data class ResponseChat(
    val data: String,
    val status: Boolean,
    val time: String,
    val type: String
)