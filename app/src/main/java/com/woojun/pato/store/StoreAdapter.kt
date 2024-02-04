package com.woojun.pato.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.woojun.pato.databinding.StoreItemBinding

class StoreAdapter(private val storeList: MutableList<Store>): RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = StoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding).also { handler->
            binding.apply {

            }
        }
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(storeList[position])
    }

    class StoreViewHolder(private val binding: StoreItemBinding):
        ViewHolder(binding.root) {
        fun bind(store: Store) {
            binding.imageView
            binding.name.text = store.name
            binding.price.text = store.price
        }
    }

}