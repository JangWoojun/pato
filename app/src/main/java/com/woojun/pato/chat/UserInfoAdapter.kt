package com.woojun.pato.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.woojun.pato.databinding.UserItemBinding

class UserInfoAdapter(private val userInfoList: MutableList<UserInfo>): RecyclerView.Adapter<UserInfoAdapter.UserInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserInfoViewHolder(binding).also { handler->
            binding.userItem.setOnClickListener {
                parent.context.startActivity(Intent(parent.context, ChattingActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return userInfoList.size
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        holder.bind(userInfoList[position])
    }

    class UserInfoViewHolder(private val binding: UserItemBinding):
        ViewHolder(binding.root) {
        fun bind(userInfo: UserInfo) {
            binding.imageView
            binding.nameText.text = userInfo.name
            binding.timeText.text = userInfo.time
            binding.lastChatText.text = userInfo.lastChat
        }
    }

}