package com.woojun.pato.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woojun.pato.auth.User

class TypeConverter {
    @androidx.room.TypeConverter
    fun fromUser(user: User): String {
        return Gson().toJson(user)
    }

    @androidx.room.TypeConverter
    fun toUser(user: String): User {
        val userType = object : TypeToken<User>() {}.type
        return Gson().fromJson(user, userType)
    }
}
