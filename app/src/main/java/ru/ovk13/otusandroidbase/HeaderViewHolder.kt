package ru.ovk13.otusandroidbase

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeaderViewHolder(private val headerView: TextView) : RecyclerView.ViewHolder(headerView) {
    fun bind(title: String) {
        headerView.text = title
    }
}