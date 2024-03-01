package com.woojun.pato.chat.dataClass

data class ResponseChat(
    val data: String,
    val status: Boolean,
    val time: String,
    val type: String,
    val opponent: Opponent?
)