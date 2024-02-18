package com.woojun.pato.auth

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.woojun.pato.R

class DropDownAdapter<String>(
    context: Context
) : ArrayAdapter<String>(context, R.layout.spinner_selected_item, R.id.spinner_text, arrayListOf()) {

    override fun getCount(): Int {
        return super.getCount() - 1
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if(position == (super.getCount() - 1)) {
            view.findViewById<TextView>(R.id.spinner_text).setTextColor(context.getColor(R.color.G3))
        }
        return view
    }

    fun submitList(list: List<String>) {
        this.clear()
        this.addAll(list)
        this.notifyDataSetChanged()
    }
}