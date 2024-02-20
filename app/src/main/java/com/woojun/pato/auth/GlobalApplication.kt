package com.woojun.pato.auth

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.woojun.pato.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppPreferences.init(this)
        KakaoSdk.init(this, BuildConfig.kakaoNativeKey)
    }
}