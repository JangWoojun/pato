package com.woojun.pato.chat

data class Chat(
    val profileImage: String = "",
    val massage: String = "",
    val isUser: Boolean = true,
    val date: String = "",
    var imageShow: Boolean = false
)