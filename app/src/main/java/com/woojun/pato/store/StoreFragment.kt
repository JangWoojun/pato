package com.woojun.pato.store

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.woojun.pato.EdgeItemDecoration
import com.woojun.pato.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf(
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
        Store(
            "티모 대위",
            "30,900원",
            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20160311_185%2Ftryandplay_1457674689918P9j4h_JPEG%2F02.JPG&type=a340",
            "https://smartstore.naver.com/unomarket_kr/products/9402668962?NaPm=ct%3Dls82bmxk%7Cci%3Dfdfe7ca506c1dfbcc337df0f296ecf937b59af35%7Ctr%3Dslsl%7Csn%3D9092384%7Chk%3D602513aa69e3ef3a46341598a77cc0ae7d7aa781"
        ),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) binding.storeRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.storeRecycler.adapter = StoreAdapter(list)
        binding.storeRecycler.addItemDecoration(EdgeItemDecoration(3, 7f.fromDpToPx()))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Float.fromDpToPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

}