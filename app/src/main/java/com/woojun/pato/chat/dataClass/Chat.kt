package com.woojun.pato.chat.dataClass

data class Chat(
    val profileImage: String = "",
    val massage: String = "",
    val isUser: Boolean = true,
    val date: String = "",
    var viewShow: Boolean = true
)