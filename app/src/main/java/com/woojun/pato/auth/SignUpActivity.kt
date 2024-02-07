package com.woojun.pato.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woojun.pato.R
import com.woojun.pato.auth.AuthUtil.isEmailValid
import com.woojun.pato.auth.AuthUtil.isIdValid
import com.woojun.pato.auth.AuthUtil.isPasswordValid
import com.woojun.pato.auth.AuthUtil.isPhoneNumberValid
import com.woojun.pato.auth.AuthUtil.isRealNameValid
import com.woojun.pato.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window.statusBarColor = statusBarColor

        initFieldSetting()

        binding.signUpButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
                .addOnCompleteListener(this@SignUpActivity) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@SignUpActivity, ProfileActivity::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(this@SignUpActivity, "오류가 발생하였습니다 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initFieldSetting() {
        binding.passwordInput.isEnabled = false
        binding.nameInput.isEnabled = false
        binding.callInput.isEnabled = false
        binding.emailInput.isEnabled = false
        binding.signUpButton.isEnabled = false

        binding.idInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isIdValid(binding.idInput.text.toString())) {
                    binding.passwordInput.isEnabled = true
                    binding.idInput.isEnabled = false

                    binding.idInput.setTextColor(Color.parseColor("#23BB75"))
                    binding.idBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))
                    binding.idIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                    binding.idLine.setBackgroundColor(Color.parseColor("#23BB75"))
                    binding.idCircle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                } else {
                    Toast.makeText(this@SignUpActivity, "영문 숫자가 혼합되어 7자리 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        binding.passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isPasswordValid(binding.passwordInput.text.toString())) {
                    binding.nameInput.isEnabled = true
                    binding.passwordInput.isEnabled = false

                    binding.passwordInput.setTextColor(Color.parseColor("#23BB75"))
                    binding.passwordBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))
                    binding.passwordIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                    binding.passwordLine.setBackgroundColor(Color.parseColor("#23BB75"))
                    binding.passwordCircle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                } else {
                    Toast.makeText(this@SignUpActivity, "영문 숫자가 혼합되어 6자리 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        binding.nameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isRealNameValid(binding.nameInput.text.toString())) {
                    binding.callInput.isEnabled = true
                    binding.nameInput.isEnabled = false

                    binding.nameInput.setTextColor(Color.parseColor("#23BB75"))
                    binding.nameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))
                    binding.nameIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                    binding.nameLine.setBackgroundColor(Color.parseColor("#23BB75"))
                    binding.nameCircle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                } else {
                    Toast.makeText(this@SignUpActivity, "한글로 최대 6자리만 가능합니다", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        binding.callInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isPhoneNumberValid(binding.callInput.text.toString())) {
                    binding.emailInput.isEnabled = true
                    binding.callInput.isEnabled = false

                    binding.callInput.setTextColor(Color.parseColor("#23BB75"))
                    binding.callBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))
                    binding.callIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                    binding.callLine.setBackgroundColor(Color.parseColor("#23BB75"))
                    binding.callCircle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                } else {
                    Toast.makeText(this@SignUpActivity, "010-1234-5678 형식으로 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        binding.emailInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isEmailValid(binding.emailInput.text.toString())) {
                    binding.signUpButton.isEnabled = true
                    binding.emailInput.isEnabled = false

                    binding.emailInput.setTextColor(Color.parseColor("#23BB75"))
                    binding.emailBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))
                    binding.emailIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))
                    binding.emailCircle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23BB75"))

                    binding.signUpButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#5666FF"))
                } else {
                    Toast.makeText(this@SignUpActivity, "이메일 형식을 지켜주세요", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }

    }
}