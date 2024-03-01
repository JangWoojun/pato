package com.woojun.pato.chat

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
        val position = chatList.size
        chatList.add(chat)

        if (position > 0) {
            val currentUserIsUser = chatList[position].isUser
            val previousUserIsUser = chatList[position - 1].isUser
            val currentDateLast = chatList[position].date.last()
            val previousDateLast = chatList[position - 1].date.last()

            if ((currentDateLast == previousDateLast) && (currentUserIsUser == previousUserIsUser)) {
                chatList[position - 1].viewShow = false
            }
            notifyItemRangeChanged(position - 1, 1)
        } else {
            notifyItemInserted(chatList.size - 1)
        }
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

    private fun Double.fromDpToPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = chatList[position]

        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams

        if (position == 0) {
            layoutParams.setMargins(0, 22.0.fromDpToPx(), 0, 0)
            holder.itemView.layoutParams = layoutParams
        } else if (chatList[position - 1].isUser == chatItem.isUser) {
            layoutParams.setMargins(0, 5.0.fromDpToPx(), 0, 0)
            holder.itemView.layoutParams = layoutParams
        } else {
            layoutParams.setMargins(0, 22.0.fromDpToPx(), 0, 0)
            holder.itemView.layoutParams = layoutParams
        }

        if (chatItem.isUser) {
            (holder as ChatViewHolder).bind(chatItem)
        } else {
            (holder as OtherChatViewHolder).bind(chatItem)
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

                if (chat.viewShow) dateText.visibility = View.VISIBLE else dateText.visibility = View.INVISIBLE

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
        fun bind(chat: Chat) {
            binding.apply{
                messageText.text = chat.massage
                dateText.text = chat.date
                Glide.with(binding.root.context)
                    .load(chat.profileImage)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageView)

                if (chat.viewShow) {
                    image.visibility = View.VISIBLE
                    dateText.visibility = View.VISIBLE
                } else {
                    image.visibility = View.INVISIBLE
                    dateText.visibility = View.INVISIBLE
                }

                dateText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            }
        }
    }

}