package com.woojun.pato.auth

data class JwtToken(
    val now: String,
    val token: String
)