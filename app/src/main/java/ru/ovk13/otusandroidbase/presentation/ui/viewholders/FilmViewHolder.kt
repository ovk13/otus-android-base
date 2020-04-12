package ru.ovk13.otusandroidbase.presentation.ui.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ovk13.otusandroidbase.R

class FilmViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val coverView: AppCompatImageView = itemView.findViewById(R.id.cover)
    private val titleView: TextView = itemView.findViewById(R.id.title)
    val toggleFavourites: ImageView = itemView.findViewById(R.id.toggleFavourites)
    private val removeFromFavourites: ImageView = itemView.findViewById(R.id.removeFromFavourites)
    val detailsBtn: Button = itemView.findViewById(R.id.detailsBtn)

    fun bind(
        title: String,
        posterPath: String?,
        visited: Boolean,
        inFavourites: Boolean
    ) {
        toggleFavourites.visibility = View.VISIBLE
        removeFromFavourites.visibility = View.GONE

        titleView.text = title
        titleView.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                if (visited) R.color.visited else R.color.title
            )
        )

        if (inFavourites) {
            toggleFavourites.alpha = 1f
        } else {
            toggleFavourites.alpha = 0.3f
        }

        Glide.with(coverView.context)
            .load(posterPath)
            .placeholder(R.drawable.ic_no_photo)
            .into(coverView)
    }
}