package ru.ovk13.otusandroidbase

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class FavouriteFilmViewHolder(itemView: View, private val positionOffset: Int) :
    RecyclerView.ViewHolder(itemView) {
    private val cover: AppCompatImageView = itemView.findViewById(R.id.cover)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val toggleFavourites: ImageView = itemView.findViewById(R.id.toggleFavourites)
    private val removeFromFavourites: ImageView = itemView.findViewById(R.id.removeFromFavourites)
    private val detailsBtn: Button = itemView.findViewById(R.id.detailsBtn)

    fun bind(
        titleResId: Int,
        coverResId: Int,
        visited: Boolean,
        clickListener: FilmViewAdapter.FilmClickListener
    ) {
        toggleFavourites.visibility = View.GONE
        removeFromFavourites.visibility = View.VISIBLE

        title.setText(titleResId)
        if (visited) {
            title.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.visited
                )
            )
        }

        cover.setImageResource(coverResId)

        removeFromFavourites.setOnClickListener {
            clickListener.onDeleteClick(
                adapterPosition,
                adapterPosition - positionOffset
            )
        }
        detailsBtn.setOnClickListener {
            clickListener.onDetailsClick(
                adapterPosition,
                adapterPosition - positionOffset
            )
        }
    }
}