package com.woojun.pato.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.pato.R
import com.woojun.pato.databinding.FragmentChatBinding

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}