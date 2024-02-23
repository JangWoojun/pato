package com.woojun.pato.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityLoginBinding
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.statusBarColor = statusBarColor

        binding.kakaoLoginButton.setOnClickListener {
            kakaoLogin(this@LoginActivity)
        }
    }

    private fun kakaoLogin(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(context, "카카오 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                kakaoLoginRequest(context, token.accessToken)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    kakaoLoginRequest(context, token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun kakaoLoginRequest(context: Context, code: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)
        val call = apiService.kakaoLogin(code)

        call.enqueue(object : Callback<JwtToken> {
            override fun onResponse(call: Call<JwtToken>, response: Response<JwtToken>) {
                if (response.isSuccessful) {
                    val token = response.body()!!.token
                    AppPreferences.token = token
                    moveNextActivity(context, token)
                } else {
                    Toast.makeText(context, "카카오 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JwtToken>, t: Throwable) {
                Toast.makeText(context, "카카오 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun moveNextActivity(context: Context, token: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)

        val call = apiService.getProfile("Bearer $token")

        call.enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                    intent.putExtra("isEdit", false)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(context, "카카오 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

}