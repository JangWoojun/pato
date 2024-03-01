package com.woojun.pato.match

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.woojun.pato.BuildConfig
import com.woojun.pato.R
import com.woojun.pato.AppPreferences
import com.woojun.pato.chat.ChattingActivity
import com.woojun.pato.databinding.FragmentChatBinding
import com.woojun.pato.match.dataClass.Matching
import com.woojun.pato.match.dataClass.MatchingWaiting
import com.woojun.pato.match.dataClass.UserInfo
import com.woojun.pato.network.RetrofitAPI
import com.woojun.pato.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
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
        if (isAdded) binding.userInfoRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.userInfoRecycler.adapter = UserInfoAdapter(list)

        binding.hiddenButton.setOnClickListener {
            findNavController().navigate(R.id.hiddenFragment)
        }

        binding.matchingStartButton.setOnClickListener {
            if (isAdded) matchingStart("Bearer ${AppPreferences.token}")
        }

        binding.setHiddenButton.setOnClickListener {
            binding.readyBox.visibility = View.INVISIBLE
            binding.hiddenGuideText.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        webSocket?.close(1000, null)
    }

    private fun matchingWaiting() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${BuildConfig.baseUrl}matching/waiting")
            .header("Authorization", "Bearer ${AppPreferences.token}")
            .build()

        val gson = Gson()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                CoroutineScope(Dispatchers.Main).launch {
                    val message = gson.fromJson(text, MatchingWaiting::class.java)

                    if (message.status) {
                        binding.loadingBox.visibility = View.INVISIBLE
                        binding.readyBox.visibility = View.VISIBLE
                        binding.setHiddenButton.visibility = View.VISIBLE
                        Log.d("확인", "이동")
                        if (isAdded) startActivity(Intent(requireContext(), ChattingActivity::class.java))
                    }
                }
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                Log.d("확인", "시작")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                webSocket.close(1000, null)
                Log.d("확인", "닫힘")
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    private fun matchingStart(header: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(RetrofitAPI::class.java)
        val call = apiService.matchingStart(header)

        call.enqueue(object : Callback<Matching> {
            override fun onResponse(call: Call<Matching>, response: Response<Matching>) {
                if (response.isSuccessful && response.body()!!.status == "matched") {
                    startActivity(Intent(requireContext(), ChattingActivity::class.java))
                } else if (response.isSuccessful && response.body()!!.status == "waiting") {
                    binding.loadingBox.visibility = View.VISIBLE
                    binding.readyBox.visibility = View.INVISIBLE
                    binding.setHiddenButton.visibility = View.INVISIBLE
                    matchingWaiting()
                } else {
                    Toast.makeText(requireContext(), "매칭에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Matching>, t: Throwable) {
                Toast.makeText(requireContext(), "매칭에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

}