package com.woojun.pato.profile.dataClass

data class ProfileRequest(
    val nickname: String,
    val region: String,
    val alcohol: Double,
    val hobby: String
)