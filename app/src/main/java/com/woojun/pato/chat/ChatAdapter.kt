package com.woojun.pato.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.woojun.pato.databinding.ChatItemBinding
import com.woojun.pato.databinding.OtherChatItemBinding

class ChatAdapter(private val chatList: MutableList<Chat>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> {
                val binding = ChatItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )

                ChatViewHolder(binding).also { handler ->
                    binding.apply {

                    }
                }
            }
            else -> {
                val binding = OtherChatItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )

                OtherChatViewHolder(binding).also { handler ->
                    binding.apply {

                    }
                }
            }

        }
    }

    fun addChat(chat: Chat) {
        chatList.add(chat)

        notifyItemInserted(chatList.size - 1)
    }

    fun getChat(): List<Chat> {
        return chatList
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].isUser) 1 else 0
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ChatViewHolder) {
            holder.init()
        } else if (holder is OtherChatViewHolder) {
            holder.init()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = chatList[position]
        if (chatItem.isUser) {
            (holder as ChatViewHolder).bind(chatItem)
        } else {
            val firstNonUserIndex = chatList.indexOfFirst { !it.isUser }
            if (position == firstNonUserIndex) {
                (holder as OtherChatViewHolder).bind(chatItem, true)
            } else {
                val otherChatList = chatList.filter { !it.isUser }
                val isFirst = chatItem.date.last() != otherChatList[otherChatList.size - 2].date.last() || chatList[position - 1].isUser
                (holder as OtherChatViewHolder).bind(chatItem, isFirst)
            }
        }
    }

    class ChatViewHolder(private val binding: ChatItemBinding):
        ViewHolder(binding.root) {

        fun init() {
            binding.apply {
                messageText.text = ""
                dateText.text = ""
            }
        }
        fun bind(chat: Chat) {
            binding.apply{
                messageText.text = chat.massage
                dateText.text = chat.date

                dateText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            }
        }
    }

    class OtherChatViewHolder(private val binding: OtherChatItemBinding):
        ViewHolder(binding.root) {

        fun init() {
            binding.apply {
                messageText.text = ""
                dateText.text = ""
                image.visibility = View.INVISIBLE
            }
        }
        fun bind(chat: Chat, isFirst: Boolean) {
            binding.apply{
                messageText.text = chat.massage
                dateText.text = chat.date

                if (isFirst) image.visibility = View.VISIBLE else image.visibility = View.INVISIBLE

                dateText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            }
        }
    }

}