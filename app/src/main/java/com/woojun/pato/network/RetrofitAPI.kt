package com.woojun.pato.network

import com.woojun.pato.auth.dataClass.CheckNicknameRequest
import com.woojun.pato.auth.dataClass.CheckResponse
import com.woojun.pato.auth.dataClass.JwtToken
import com.woojun.pato.profile.dataClass.Profile
import com.woojun.pato.profile.dataClass.ProfileImageRequest
import com.woojun.pato.profile.dataClass.ProfileRequest
import com.woojun.pato.match.dataClass.Matching
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("oauth/kakao/login")
    fun kakaoLogin(
        @Query("code") code: String,
    ): Call<JwtToken>

    @GET("auth/test/login")
    fun testLogin(
        @Query("code") code: String,
    ): Call<JwtToken>

    @GET("profile/get")
    fun getProfile(
        @Header("Authorization") authorization: String,
    ): Call<Profile>

    @POST("profile/check-nickname")
    fun duplicateCheck(
        @Body nickname: CheckNicknameRequest,
    ): Call<CheckResponse>

    @POST("profile/save")
    fun setProfile(
        @Header("Authorization") authorization: String,
        @Body profileRequest: ProfileRequest
    ): Call<CheckResponse>

    @POST("profile/saveImage")
    fun setProfileImage(
        @Header("Authorization") authorization: String,
        @Body image: ProfileImageRequest
    ): Call<CheckResponse>

    @GET("matching/start")
    fun matchingStart(
        @Header("Authorization") authorization: String,
    ): Call<Matching>

}
