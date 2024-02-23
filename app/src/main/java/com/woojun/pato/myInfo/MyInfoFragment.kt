package com.woojun.pato.myInfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.woojun.pato.auth.AppPreferences
import com.woojun.pato.auth.Profile
import com.woojun.pato.auth.ProfileActivity
import com.woojun.pato.databinding.FragmentMyInfoBinding
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoFragment : Fragment() {

    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfile(requireContext(), AppPreferences.token)

        binding.editButton.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("isEdit", true)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProfile(context: Context, token: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)

        val call = apiService.getProfile("Bearer $token")

        call.enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    bindingProfile(response.body()!!)
                } else {
                    Toast.makeText(context, "프로필 불러오기를 실패였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(context, "프로필 불러오기를 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindingProfile(profile: Profile)  {
        binding.nicknameText.text = profile.nickname
        binding.locationText.text = profile.region
        binding.drinkingCapacityText.text = when(profile.alcohol) {
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
        binding.hobbyText.text = profile.hobby
    }

}