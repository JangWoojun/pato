package com.woojun.pato.chat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.pato.BuildConfig
import com.woojun.pato.R
import com.woojun.pato.auth.AppPreferences
import com.woojun.pato.databinding.FragmentChatBinding
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import com.woojun.pato.network.WaitingWebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf(
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
        UserInfo("", "라윤지", "오후 5:54", "안녕하세요! 새로운 컨셉 문의를 위해 연락 드립니다"),
    )
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userInfoRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.userInfoRecycler.adapter = UserInfoAdapter(list)

        binding.hiddenButton.setOnClickListener {
            findNavController().navigate(R.id.hiddenFragment)
        }

        binding.matchingStartButton.setOnClickListener {
            matchingStart("Bearer ${AppPreferences.token}")
        }

        binding.setHiddenButton.setOnClickListener {
            binding.readyBox.visibility = View.INVISIBLE
            binding.hiddenGuideText.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        webSocket?.close(1000, "액티비티 종료")
    }

    private fun matchingWaiting() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${BuildConfig.baseUrl}matching/waiting")
            .header("Authorization", "Bearer ${AppPreferences.token}")
            .build()

        val listener = WaitingWebSocketListener(requireContext()) {
            binding.loadingBox.visibility = View.INVISIBLE
            binding.readyBox.visibility = View.VISIBLE
            binding.setHiddenButton.visibility = View.VISIBLE
            startActivity(Intent(requireContext(), ChattingActivity::class.java))

            client.dispatcher.executorService.shutdown()
        }

        webSocket = client.newWebSocket(request, listener)
        webSocket?.close(1000, "매칭 완료")
    }

    private fun matchingStart(header: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)
        val call = apiService.matchingStart(header)

        call.enqueue(object : Callback<Matching> {
            override fun onResponse(call: Call<Matching>, response: Response<Matching>) {
                if (response.isSuccessful && response.body()!!.status == "matched") {
                    startActivity(Intent(requireContext(), ChattingActivity::class.java))
                } else if (response.body()!!.status == "waiting") {
                    binding.loadingBox.visibility = View.VISIBLE
                    binding.readyBox.visibility = View.INVISIBLE
                    binding.setHiddenButton.visibility = View.INVISIBLE
                    matchingWaiting()
                } else {
                    Toast.makeText(context, "매칭에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Matching>, t: Throwable) {
                Toast.makeText(context, "매칭에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

}