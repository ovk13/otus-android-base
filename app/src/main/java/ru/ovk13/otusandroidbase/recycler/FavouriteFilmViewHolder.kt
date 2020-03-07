package ru.ovk13.otusandroidbase.recycler

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ovk13.otusandroidbase.R

class FavouriteFilmViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val cover: AppCompatImageView = itemView.findViewById(R.id.cover)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val toggleFavourites: ImageView = itemView.findViewById(R.id.toggleFavourites)
    val removeFromFavourites: ImageView = itemView.findViewById(R.id.removeFromFavourites)
    val detailsBtn: Button = itemView.findViewById(R.id.detailsBtn)

    fun bind(
        titleResId: Int,
        coverResId: Int,
        visited: Boolean
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
    }
}