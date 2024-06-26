package com.woojun.pato.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.woojun.pato.AppPreferences
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.auth.dataClass.JwtToken
import com.woojun.pato.databinding.ActivityLoginBinding
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import com.woojun.pato.profile.dataClass.Profile
import com.woojun.pato.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var testLoginCheck = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.statusBarColor = statusBarColor

        binding.kakaoLoginButton.setOnClickListener {
            kakaoLogin(this@LoginActivity)
        }

        binding.testLoginButton.setOnClickListener {
            testLoginCheck+=1
            if (testLoginCheck > 4) {
                Toast.makeText(this@LoginActivity, "테스트 로그인을 사용합니다", Toast.LENGTH_SHORT).show()
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.test_login_dialog, null)

        val testCodeInput = dialogView.findViewById<EditText>(R.id.editTextDialogUserInput)

        val dialog = AlertDialog.Builder(this)
            .setTitle("테스트 로그인")
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                val testCode = testCodeInput.text.toString()
                testLogin(this@LoginActivity, testCode)
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
    }

    private fun testLogin(context: Context, code: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)
        val call = apiService.testLogin(code)

        call.enqueue(object : Callback<JwtToken> {
            override fun onResponse(call: Call<JwtToken>, response: Response<JwtToken>) {
                if (response.isSuccessful) {
                    val token = response.body()!!.token
                    AppPreferences.token = token
                    moveNextActivity(context, token)
                } else {
                    Toast.makeText(context, "테스트 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JwtToken>, t: Throwable) {
                Toast.makeText(context, "테스트 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
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
                    AppPreferences.profile = true
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                    intent.putExtra("isEdit", false)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(context, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

}