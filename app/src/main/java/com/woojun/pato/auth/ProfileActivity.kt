package com.woojun.pato.auth

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityProfileBinding
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val citiesList = listOf(
        "서울특별시",
        "부산광역시",
        "대구광역시",
        "인천광역시",
        "광주광역시",
        "대전광역시",
        "울산광역시",
        "세종특별자치시",
        "경기도",
        "강원특별자치도",
        "충청북도",
        "충청남도",
        "전북특별자치도",
        "전라남도",
        "경상북도",
        "경상남도",
        "제주특별자치도",
        "시/도"
    )
    private val townMap = mapOf(
        "서울특별시" to listOf(
            "종로구",
            "중구",
            "용산구",
            "성동구",
            "광진구",
            "동대문구",
            "중랑구",
            "성북구",
            "강북구",
            "도봉구",
            "노원구",
            "은평구",
            "서대문구",
            "마포구",
            "양천구",
            "강서구",
            "구로구",
            "금천구",
            "영등포구",
            "동작구",
            "관악구",
            "서초구",
            "강남구",
            "송파구",
            "강동구",
            "시/군/구"
        ),
        "부산광역시" to listOf(
            "중구",
            "서구",
            "동구",
            "영도구",
            "부산진구",
            "동래구",
            "남구",
            "북구",
            "해운대구",
            "사하구",
            "금정구",
            "강서구",
            "연제구",
            "수영구",
            "사상구",
            "기장군",
            "시/군/구"
        ),
        "대구광역시" to listOf("중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군", "군위군", "시/군/구"),
        "인천광역시" to listOf(
            "중구",
            "동구",
            "미추홀구",
            "연수구",
            "남동구",
            "부평구",
            "계양구",
            "서구",
            "강화군",
            "옹진군",
            "시/군/구"
        ),
        "광주광역시" to listOf("동구", "서구", "남구", "북구", "광산구", "시/군/구"),
        "대전광역시" to listOf("동구", "중구", "서구", "유성구", "대덕구", "시/군/구"),
        "울산광역시" to listOf("중구", "남구", "동구", "북구", "울주군", "시/군/구"),
        "세종특별자치시" to listOf("세종시", "시/군/구"),
        "경기도" to listOf(
            "수원시",
            "용인시",
            "고양시",
            "화성시",
            "성남시",
            "부천시",
            "남양주시",
            "안산시",
            "평택시",
            "안양시",
            "흥시",
            "파주시",
            "김포시",
            "의정부시",
            "광주시",
            "하남시",
            "광명시",
            "군포시",
            "양주시",
            "오산시",
            "이천시",
            "안성시",
            "구리시",
            "의왕시",
            "포천시",
            "양평군",
            "여주시",
            "동두천시",
            "과천시",
            "가평군",
            "연천군",
            "시/군/구"
        ),
        "강원특별자치도" to listOf(
            "춘천시",
            "원주시",
            "강릉시",
            "동해시",
            "태백시",
            "속초시",
            "삼척시",
            "홍천군",
            "횡성군",
            "영월군",
            "평창군",
            "정선군",
            "철원군",
            "화천군",
            "양구군",
            "인제군",
            "고성군",
            "양양군",
            "시/군/구"
        ),
        "충청북도" to listOf(
            "청주시",
            "충주시",
            "제천시",
            "보은군",
            "옥천군",
            "영동군",
            "증평군",
            "진천군",
            "괴산군",
            "음성군",
            "단양군",
            "시/군/구"
        ),
        "충청남도" to listOf(
            "천안시",
            "공주시",
            "보령시",
            "아산시",
            "서산시",
            "논산시",
            "계룡시",
            "당진시",
            "금산군",
            "부여군",
            "서천군",
            "청양군",
            "홍성군",
            "예산군",
            "태안군",
            "시/군/구"
        ),
        "전북특별자치도" to listOf(
            "전주시",
            "군산시",
            "익산시",
            "정읍시",
            "남원시",
            "김제시",
            "완주군",
            "진안군",
            "무주군",
            "장수군",
            "임실군",
            "순창군",
            "고창군",
            "부안군",
            "시/군/구"
        ),
        "전라남도" to listOf(
            "목포시",
            "여수시",
            "순천시",
            "나주시",
            "광양시",
            "담양군",
            "곡성군",
            "구례군",
            "고흥군",
            "보성군",
            "화순군",
            "장흥군",
            "강진군",
            "해남군",
            "영암군",
            "무안군",
            "함평군",
            "영광군",
            "장성군",
            "완도군",
            "진도군",
            "신안군",
            "시/군/구"
        ),
        "경상북도" to listOf(
            "포항시",
            "경주시",
            "김천시",
            "안동시",
            "구미시",
            "영주시",
            "영천시",
            "상주시",
            "문경시",
            "경산시",
            "의성군",
            "청송군",
            "영양군",
            "영덕군",
            "청도군",
            "고령군",
            "성주군",
            "칠곡군",
            "예천군",
            "봉화군",
            "울진군",
            "울릉군",
            "시/군/구"
        ),
        "경상남도" to listOf(
            "창원시",
            "진주시",
            "통영시",
            "사천시",
            "김해시",
            "밀양시",
            "거제시",
            "양산시",
            "의령군",
            "함안군",
            "창녕군",
            "고성군",
            "남해군",
            "하동군",
            "산청군",
            "함양군",
            "거창군",
            "합천군",
            "시/군/구"
        ),
        "제주특별자치도" to listOf("제주시", "서귀포시", "시/군/구"),
    )
    private val alcoholList = listOf(
        "선택 없음",
        "소주 0.5병 이하",
        "소주 1병",
        "소주 2병",
        "소주 3병",
        "소주 4병",
        "소주 5병",
        "소주 5병 이상",
        "소주 기준"
    )


    private var location1 = ""
    private var location2 = ""
    private var alcohol = 0.0
    private var nickNameCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            binding.editText1.visibility = View.VISIBLE
            binding.editText2.visibility = View.VISIBLE
        } else {
            binding.signUpText1.visibility = View.VISIBLE
            binding.signUpText2.visibility = View.VISIBLE
            binding.signUpText3.visibility = View.VISIBLE
        }

        val statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.statusBarColor = statusBarColor

        binding.finishButton.isEnabled = false
        binding.finishButton.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finishAffinity()
        }

        binding.nicknameInput.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.nicknameInput.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.nicknameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nickNameCheck = false
                binding.nicknameInput.setTextColor(Color.parseColor("#000000"))
                binding.nicknameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#C4C4C4")))

                binding.duplicateCheckText.text = ""

                isFinish()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.duplicateCheckButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val nickName = binding.nicknameInput.text.toString()

                val spacesCheck = containsNoSpaces(nickName)
                val duplicateCheck = duplicateCheck(nickName)
                val koreanCheck = isKoreanName(nickName)

                withContext(Dispatchers.Main) {
                    if (spacesCheck && duplicateCheck && koreanCheck) {
                        binding.nicknameInput.setTextColor(Color.parseColor("#23BB75"))
                        binding.nicknameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))

                        binding.duplicateCheckText.visibility = View.VISIBLE
                        binding.duplicateCheckText.setTextColor(Color.parseColor("#23BB75"))
                        binding.duplicateCheckText.text = "확인되었습니다."

                        nickNameCheck = true

                        isFinish()
                    } else {
                        binding.nicknameInput.setTextColor(Color.parseColor("#B10000"))
                        binding.nicknameBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#B10000")))

                        binding.duplicateCheckText.visibility = View.VISIBLE
                        binding.duplicateCheckText.setTextColor(Color.parseColor("#C4C4C4"))

                        nickNameCheck = false

                        if (!spacesCheck) binding.duplicateCheckText.text = "공백 불가"
                        if (!duplicateCheck) binding.duplicateCheckText.text = "중복 불가"
                        if (!koreanCheck) binding.duplicateCheckText.text = "한글만 허용"
                    }
                }

            }

        }

        binding.hobbyInput.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                if (binding.hobbyInput.text.isNotEmpty()) {
                    binding.hobbyInput.setTextColor(Color.parseColor("#23BB75"))
                    binding.hobbyBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#23BB75")))
                } else {
                    binding.hobbyInput.setTextColor(Color.parseColor("#B10000"))
                    binding.hobbyBox.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#B10000")))
                }
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.hobbyInput.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        val location1Adapter = DropDownAdapter<String>(this@ProfileActivity)
        location1Adapter.setDropDownViewResource(R.layout.spinner_item)
        location1Adapter.submitList(citiesList)

        val location2Adapter = DropDownAdapter<String>(this@ProfileActivity)
        location2Adapter.setDropDownViewResource(R.layout.spinner_item)
        location2Adapter.submitList(townMap["서울특별시"]!!)

        binding.locationSpinner1.adapter = location1Adapter
        binding.locationSpinner1.setSelection(location1Adapter.count)

        binding.locationSpinner1.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != location1Adapter.count) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        location1 = selectedItem

                        binding.locationSpinner1.setBackgroundResource(R.drawable.dropdown_selected_background)
                        location2Adapter.submitList(townMap[selectedItem]!!)

                        binding.locationSpinner2.isEnabled = true
                        binding.locationSpinner2.adapter = location2Adapter
                        binding.locationSpinner2.setSelection(location2Adapter.count)

                        isFinish()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        binding.locationSpinner2.isEnabled = false
        binding.locationSpinner2.adapter = location2Adapter
        binding.locationSpinner2.setSelection(location2Adapter.count)

        binding.locationSpinner2.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != location2Adapter.count) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        location2 = selectedItem

                        binding.locationSpinner2.setBackgroundResource(R.drawable.dropdown_selected_background)

                        isFinish()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        val alcoholAdapter = DropDownAdapter<String>(this@ProfileActivity)
        alcoholAdapter.setDropDownViewResource(R.layout.spinner_item)
        alcoholAdapter.submitList(alcoholList)

        binding.alcoholSpinner.adapter = alcoholAdapter
        binding.alcoholSpinner.setSelection(alcoholAdapter.count)

        binding.alcoholSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position != alcoholAdapter.count) {
                        binding.alcoholSpinner.setBackgroundResource(R.drawable.dropdown_selected_background)
                    }
                    when (position) {
                        0 -> {
                            alcohol = 0.0
                        }

                        1 -> {
                            alcohol = 0.5
                        }

                        2 -> {
                            alcohol = 1.0
                        }

                        3 -> {
                            alcohol = 2.0
                        }

                        4 -> {
                            alcohol = 3.0
                        }

                        5 -> {
                            alcohol = 4.0
                        }

                        6 -> {
                            alcohol = 5.0
                        }

                        7 -> {
                            alcohol = 5.5
                        }

                        else -> {
                            alcohol = 0.0
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun isKoreanName(nickName: String): Boolean {
        val regex = Regex("^[가-힣]+$")
        return regex.matches(nickName)
    }

    private fun containsNoSpaces(nickName: String): Boolean {
        return !nickName.contains(" ")
    }

    private fun duplicateCheck(nickName: String): Boolean {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)

        val nickname = CheckNicknameRequest(nickName)

        return try {
            val response = apiService.duplicateCheck(nickname).execute()
            response.isSuccessful && !response.body()!!.status
        } catch (e: Exception) {
            false
        }
    }

    private fun isFinish() {
        if (binding.nicknameInput.text.isNotEmpty() && location1 != "" && location2 != "" && nickNameCheck) {
            binding.finishButton.isEnabled = true
            binding.finishButton.setCardBackgroundColor(Color.parseColor("#FF5656"))
        } else {
            binding.finishButton.isEnabled = false
            binding.finishButton.setCardBackgroundColor(Color.parseColor("#C4C4C4"))
        }
    }

}