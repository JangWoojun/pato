package com.woojun.pato.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.auth.AuthUtil.getUid
import com.woojun.pato.database.AppDatabase
import com.woojun.pato.databinding.ActivityProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var storage: FirebaseStorage

    private var sex = "남성"
    private var date = ""
    private lateinit var imageUri: Uri
    private var profileImageUrl = ""

    private val getContent: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                uploadImageToFirebaseStorage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage

        val statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window.statusBarColor = statusBarColor

        binding.profileButton.setOnClickListener {
            getContent.launch("image/*")
        }

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
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(this@ProfileActivity)
                val user = User(
                    getUid(),
                    profileImageUrl,
                    binding.nicknameInput.text.toString(),
                    date,
                    sex,
                    binding.locationInput.text.toString()
                )
                db?.userDao()!!.insertUser(user)
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }

    private fun showYearPickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, _, _ ->
                date = selectedYear.toString()
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
            && binding.locationInput.text != "지역을 선택해주세요..." && profileImageUrl != ""
        ) {
            binding.finishButton.isEnabled = true
            binding.finishButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#5666FF"))
        } else {
            binding.finishButton.isEnabled = false
            binding.finishButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C4C4C4"))
            binding.nicknameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#C4C4C4")))
        }
    }

    private fun uploadImageToFirebaseStorage() {
        val storageReference = storage.reference.child("profile/${getUid()}.jpg")

        storageReference.putFile(imageUri)
            .addOnSuccessListener {taskSnapshot ->
                val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                if (downloadUrl != null) {
                    profileImageUrl = downloadUrl.toString()

                    binding.imageView1.visibility = View.GONE
                    Glide.with(this@ProfileActivity)
                        .load(imageUri)
                        .apply(RequestOptions
                            .centerCropTransform()
                        )
                        .into(binding.imageView2)
                }
            }
            .addOnFailureListener { _ ->
                Toast.makeText(this@ProfileActivity, "이미지 업로드를 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
    }
}