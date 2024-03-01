package com.woojun.pato.auth.dataClass

data class JwtToken(
    val now: String,
    val token: String
)