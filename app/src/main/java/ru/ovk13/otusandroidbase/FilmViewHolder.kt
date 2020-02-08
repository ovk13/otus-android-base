package ru.ovk13.otusandroidbase

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView

class FilmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val cover: AppCompatImageView = itemView.findViewById(R.id.cover)
    val title: TextView = itemView.findViewById(R.id.title)
    val detailsBtn: Button = itemView.findViewById(R.id.detailsBtn)

    fun bind(
        coverDrawable: Drawable?,
        titleText: String,
        clickListener: FilmViewAdapter.FilmClickListener
    ) {
        cover.setImageDrawable(coverDrawable)
        title.text = titleText

        detailsBtn.setOnClickListener { clickListener.onFilmClick(adapterPosition) }
    }
}