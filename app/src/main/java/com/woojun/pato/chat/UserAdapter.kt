package com.woojun.pato.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.woojun.pato.databinding.UserItemBinding

class UserAdapter(private val userList: MutableList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding).also { handler->
            binding.userItem.setOnClickListener {
                parent.context.startActivity(Intent(parent.context, ChattingActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    class UserViewHolder(private val binding: UserItemBinding):
        ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.imageView
            binding.nameText.text = user.name
            binding.timeText.text = user.time
            binding.lastChatText.text = user.lastChat
        }
    }

}