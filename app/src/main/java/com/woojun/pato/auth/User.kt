package com.woojun.pato.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val uid: String,
    val imageUrl: String = "",
    val nickname: String,
    val age: String,
    val sex: String,
    val location: String,
)
