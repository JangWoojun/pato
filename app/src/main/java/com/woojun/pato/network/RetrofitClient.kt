package com.woojun.pato.network

import com.google.gson.GsonBuilder
import com.woojun.pato.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    fun getInstance(): Retrofit {
        if(instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BuildConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return instance!!
    }
}