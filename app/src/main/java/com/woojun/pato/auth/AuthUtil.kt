package com.woojun.pato.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthUtil {
    fun getUid(): String {
        val auth = Firebase.auth
        return auth.uid.toString()
    }
    fun isIdValid(id: String): Boolean {
        // 아이디는 영어와 숫자가 섞여야 하며 7자리 이상
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}\$")
        return regex.matches(id)
    }

    fun isPasswordValid(password: String): Boolean {
        // 비밀번호는 영어와 숫자가 섞여야 하며 6자리 이상
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$")
        return regex.matches(password)
    }

    fun isRealNameValid(realName: String): Boolean {
        // 실명은 한국어로만 적어야 하며 6자리 제한
        val regex = Regex("^[가-힣]{1,6}\$")
        return regex.matches(realName)
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        // 전화번호는 010-1234-5678 형식
        val regex = Regex("^010-\\d{4}-\\d{4}\$")
        return regex.matches(phoneNumber)
    }

    fun isEmailValid(email: String): Boolean {
        // 이메일 형식
        val regex = Regex("^\\S+@\\S+\\.\\S+\$")
        return regex.matches(email)
    }
}