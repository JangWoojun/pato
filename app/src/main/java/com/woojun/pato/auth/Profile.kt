package com.woojun.pato.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val alcohol: Double,
    val hobby: String,
    val image: String,
    val nickname: String,
    val region: String,
    val uuid: String
): Parcelable