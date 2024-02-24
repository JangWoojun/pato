package com.woojun.pato.auth

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.woojun.pato.MainActivity
import com.woojun.pato.R
import com.woojun.pato.databinding.ActivityProfileBinding
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val citiesList = arrayOf(
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
    )
    private val townMap = mapOf(
        "서울특별시" to arrayOf(
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
        ),
        "부산광역시" to arrayOf(
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
        ),
        "대구광역시" to arrayOf("중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군", "군위군"),
        "인천광역시" to arrayOf(
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
        ),
        "광주광역시" to arrayOf("동구", "서구", "남구", "북구", "광산구"),
        "대전광역시" to arrayOf("동구", "중구", "서구", "유성구", "대덕구"),
        "울산광역시" to arrayOf("중구", "남구", "동구", "북구", "울주군"),
        "세종특별자치시" to arrayOf("세종시"),
        "경기도" to arrayOf(
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
        ),
        "강원특별자치도" to arrayOf(
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
        ),
        "충청북도" to arrayOf(
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
        ),
        "충청남도" to arrayOf(
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
        ),
        "전북특별자치도" to arrayOf(
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
        ),
        "전라남도" to arrayOf(
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
        ),
        "경상북도" to arrayOf(
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
        ),
        "경상남도" to arrayOf(
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
        ),
        "제주특별자치도" to arrayOf("제주시", "서귀포시"),
        "" to arrayOf("")
    )
    private val alcoholList = arrayOf(
        "선택 없음",
        "소주 0.5병 이하",
        "소주 1병",
        "소주 2병",
        "소주 3병",
        "소주 4병",
        "소주 5병",
        "소주 5병 이상",
    )

    private var location1 = ""
    private var location2 = ""
    private var alcohol = 0.0
    private var nickNameCheck = false

    private var image: String = ""

    private val getContent: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                image = uriToBase64(this@ProfileActivity, it)
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.defaultProfileImage.visibility = View.VISIBLE
                            binding.profileImage.visibility = View.INVISIBLE
                            return false
                        }
                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.defaultProfileImage.visibility = View.INVISIBLE
                            binding.profileImage.visibility = View.VISIBLE
                            return false
                        }
                    })
                    .into(binding.profileImage)
            }
            isFinish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            binding.editText1.visibility = View.VISIBLE
            binding.editText2.visibility = View.VISIBLE

            val profile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("profile", Profile::class.java)!!
            } else {
                intent.getParcelableExtra("profile")!!
            }

            location1 = profile.region.split(" ")[0]
            location2 = profile.region.split(" ")[1]
            alcohol = profile.alcohol
            image = profile.image

            nickNameCheck = true
            binding.finishButton.isEnabled = true

            binding.nicknameInput.setText(profile.nickname)
            binding.locationSpinner1Text.text = profile.region.split(" ")[0]
            binding.locationSpinner2Text.text = profile.region.split(" ")[1]
            binding.alcoholSpinnerText.text = when(profile.alcohol) {
                0.0 -> "선택 없음"
                0.5 -> "소주 0.5병 이하"
                1.0 -> "소주 1병"
                2.0 -> "소주 2병"
                3.0 -> "소주 3병"
                4.0 -> "소주 4병"
                5.0 -> "소주 5병"
                5.5 -> "소주 5병 이상"
                else -> "선택 없음"
            }
            binding.hobbyInput.setText(profile.hobby)
            binding.defaultProfileImage.visibility = View.INVISIBLE
            Glide.with(this)
                .load(profile.image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.profileImage)
            isFinish()
        } else {
            binding.signUpText1.visibility = View.VISIBLE
            binding.signUpText2.visibility = View.VISIBLE
            binding.signUpText3.visibility = View.VISIBLE
            binding.finishButton.isEnabled = false
        }

        val statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.statusBarColor = statusBarColor

        binding.profileButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.finishButton.setOnClickListener {
            setProfile(
                this@ProfileActivity,
                "Bearer ${AppPreferences.token}",
                binding.nicknameInput.text.toString(),
                "$location1 $location2",
                alcohol,
                binding.hobbyInput.text.toString(),
                image
            )
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
                binding.duplicateCheckButton.isEnabled = true
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

                        binding.duplicateCheckButton.isEnabled = false
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

        binding.locationSpinner1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("시/도")
                .setItems(citiesList) { _, index ->
                    location1 = citiesList[index]
                    location2 = ""

                    binding.locationSpinner1Text.text = location1
                    binding.locationSpinner2Text.text = "시/군/구"

                    binding.locationSpinner1.strokeColor = Color.parseColor("#23BB75")
                    binding.locationSpinner1Text.setTextColor(Color.parseColor("#23BB75"))
                    binding.locationSpinner1Icon.setColorFilter(Color.parseColor("#23BB75"))

                    binding.locationSpinner2.strokeColor = Color.parseColor("#C4C4C4")
                    binding.locationSpinner2Text.setTextColor(Color.parseColor("#C4C4C4"))
                    binding.locationSpinner2Icon.setColorFilter(Color.parseColor("#C4C4C4"))

                    binding.locationSpinner2.isEnabled = true
                    setLocation2Dialog(location1)

                    isFinish()
                }
            builder.show()
        }

        binding.locationSpinner2.isEnabled = false
        binding.locationSpinner2.setOnClickListener {
            setLocation2Dialog(location1)
        }

        binding.alcoholSpinner.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("소준 기준")
                .setItems(alcoholList) { _, index ->
                    alcohol = when (index) {
                        0 -> 0.0
                        1 -> 0.5
                        2 -> 1.0
                        3 -> 2.0
                        4 -> 3.0
                        5 -> 4.0
                        6 -> 5.0
                        7 -> 5.5
                        else -> 0.0
                    }
                    binding.alcoholSpinnerText.text = alcoholList[index]

                    binding.alcoholSpinner.strokeColor = Color.parseColor("#23BB75")
                    binding.alcoholSpinnerText.setTextColor(Color.parseColor("#23BB75"))
                    binding.alcoholSpinnerIcon.setColorFilter(Color.parseColor("#23BB75"))
                }
            dialog.show()
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


    }

    private fun setLocation2Dialog(item: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("시/군/구")
            .setItems(townMap[item]) { _, index ->
                location2 = townMap[item]!![index]
                binding.locationSpinner2Text.text = location2

                binding.locationSpinner2.strokeColor = Color.parseColor("#23BB75")
                binding.locationSpinner2Text.setTextColor(Color.parseColor("#23BB75"))
                binding.locationSpinner2Icon.setColorFilter(Color.parseColor("#23BB75"))

                isFinish()
            }
        dialog.show()
    }

    private fun setProfileImage(context: Context, header: String, image: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)
        val call = apiService.setProfileImage(header, ProfileImageRequest(image))

        call.enqueue(object : Callback<CheckResponse> {
            override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    moveMainActivity()
                } else {
                    Toast.makeText(context, "이미지 설정에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                Toast.makeText(context, "이미지 설정에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uriToBase64(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }
        inputStream?.close()

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun setProfile(
        context: Context, header: String, nickName: String, region: String, alcohol: Double, hobby: String, image: String
    ) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)
        val call = apiService.setProfile(header, ProfileRequest(nickName, region, alcohol, hobby))

        call.enqueue(object : Callback<CheckResponse> {
            override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    setProfileImage(context, header, image)
                } else {
                    Toast.makeText(context, "프로필 설정에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                Toast.makeText(context, "프로필 설정에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun moveMainActivity() {
        AppPreferences.profile = true
        startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        finishAffinity()
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
        if (binding.nicknameInput.text.isNotEmpty() && location1 != "" && location2 != "" && nickNameCheck && image != "") {
            binding.finishButton.isEnabled = true
            binding.finishButton.setCardBackgroundColor(Color.parseColor("#FF5656"))
        } else {
            binding.finishButton.isEnabled = false
            binding.finishButton.setCardBackgroundColor(Color.parseColor("#C4C4C4"))
        }
    }

}