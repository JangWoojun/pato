package com.woojun.pato.auth

data class ProfileRequest(
    val nickname: String,
    val region: String,
    val alcohol: Double,
    val hobby: String
)