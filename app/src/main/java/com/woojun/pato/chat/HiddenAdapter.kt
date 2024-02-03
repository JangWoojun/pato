package com.woojun.pato.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.woojun.pato.databinding.HiddenItemBinding
import com.woojun.pato.databinding.UserItemBinding

class HiddenAdapter(private val hiddenList: MutableList<Hidden>): RecyclerView.Adapter<HiddenAdapter.HiddenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiddenViewHolder {
        val binding = HiddenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HiddenViewHolder(binding).also { handler->
            binding.apply {

            }
        }
    }

    override fun getItemCount(): Int {
        return hiddenList.size
    }

    override fun onBindViewHolder(holder: HiddenViewHolder, position: Int) {
        holder.bind(hiddenList[position])
    }

    class HiddenViewHolder(private val binding: HiddenItemBinding):
        ViewHolder(binding.root) {
        fun bind(hidden: Hidden) {
            binding.imageView
            binding.nameAgeText.text = hidden.nameAge
            binding.locationText.text = hidden.location
        }
    }

}