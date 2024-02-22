package com.woojun.pato.auth

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "AppPreferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val TOKEN = Pair("token", "")
    private val PROFILE = Pair("profile", false)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var token: String
        get() = preferences.getString(TOKEN.first, TOKEN.second) ?: ""
        set(value) = preferences.edit {
            it.putString(TOKEN.first, value)
        }
    var profile: Boolean
        get() = preferences.getBoolean(PROFILE.first, PROFILE.second)
        set(value) = preferences.edit {
            it.putBoolean(PROFILE.first, value)
        }
}