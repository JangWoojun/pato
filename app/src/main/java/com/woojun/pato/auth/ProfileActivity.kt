package com.woojun.pato.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var sex = "남성"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window.statusBarColor = statusBarColor

        binding.finishButton.isEnabled = false

        binding.nicknameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                binding.nicknameInput.setTextColor(Color.parseColor("#5666FF"))
                binding.nicknameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#5666FF")))
                isFinish()
            }
        })

        binding.ageBox.setOnClickListener {
            showYearPickerDialog()
            isFinish()
        }

        binding.manButton.setOnClickListener {
            it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#5666FF"))
            binding.womanButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C4C4C4"))
            sex = "남성"
        }

        binding.womanButton.setOnClickListener {
            it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#5666FF"))
            binding.manButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C4C4C4"))
            sex = "여성"
        }

        val layoutInflater = LayoutInflater.from(this@ProfileActivity)
        val popupView = layoutInflater.inflate(R.layout.dropdown_menu, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        binding.locationBox.setOnClickListener {
            popupWindow.showAsDropDown(it)
        }

        popupView.findViewById<CardView>(R.id.gangnam_box).setOnClickListener {
            binding.locationInput.text = "서울특별시 강남구"
            binding.locationInput.setTextColor(Color.parseColor("#5666FF"))
            popupWindow.dismiss()
            isFinish()
        }

        popupView.findViewById<CardView>(R.id.gangbuk_box).setOnClickListener {
            binding.locationInput.text = "서울특별시 강북구"
            binding.locationInput.setTextColor(Color.parseColor("#5666FF"))
            popupWindow.dismiss()
            isFinish()
        }

        popupView.findViewById<CardView>(R.id.gangdong_box).setOnClickListener {
            binding.locationInput.text = "서울특별시 강동구"
            binding.locationInput.setTextColor(Color.parseColor("#5666FF"))
            popupWindow.dismiss()
            isFinish()
        }

        popupView.findViewById<CardView>(R.id.gangseo_box).setOnClickListener {
            binding.locationInput.text = "서울특별시 강서구"
            binding.locationInput.setTextColor(Color.parseColor("#5666FF"))
            popupWindow.dismiss()
            isFinish()
        }

        popupView.findViewById<CardView>(R.id.gwanak_box).setOnClickListener {
            binding.locationInput.text = "서울특별시 관악구"
            binding.locationInput.setTextColor(Color.parseColor("#5666FF"))
            popupWindow.dismiss()
            isFinish()
        }

        binding.finishButton.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun showYearPickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, _, _ ->
                binding.ageInput.text = "${selectedYear}년생 (만 ${currentYear - selectedYear}세)"
                binding.ageInput.setTextColor(Color.parseColor("#5666FF"))
                binding.ageBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#5666FF")))
            },
            currentYear,
            0,
            1
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.datePicker.init(currentYear, 0, 1, null)

        datePickerDialog.show()
    }

    private fun isFinish() {
        if (
            binding.nicknameInput.text.isNotEmpty() && binding.ageInput.text != "태어난 연도를 선택해주세요..."
            && binding.locationInput.text != "지역을 선택해주세요..."
        ) {
            binding.finishButton.isEnabled = true
            binding.finishButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#5666FF"))
        } else {
            binding.finishButton.isEnabled = false
            binding.finishButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C4C4C4"))
            binding.nicknameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#C4C4C4")))
        }
    }
}