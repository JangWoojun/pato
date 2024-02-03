package com.woojun.pato.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.woojun.pato.R
import com.woojun.pato.databinding.FragmentHiddenBinding

class HiddenFragment : Fragment() {
    private var _binding: FragmentHiddenBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf(
        Hidden(
            "",
            "최수빈(24)",
            "5km 내외"
        ),
        Hidden(
            "",
            "최수빈(24)",
            "5km 내외"
        ),
        Hidden(
            "",
            "최수빈(24)",
            "5km 내외"
        ),
        Hidden(
            "",
            "최수빈(24)",
            "5km 내외"
        ),
        Hidden(
            "",
            "최수빈(24)",
            "5km 내외"
        ),
        Hidden(
            "",
            "최수빈(24)",
            "5km 내외"
        ),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHiddenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.userRecycler.adapter = HiddenAdapter(list)

        binding.backButton.setOnClickListener {
            findNavController().navigate(
                R.id.chatFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.bottom_navigation_graph, false)
                    .build())
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}