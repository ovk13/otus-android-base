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

abstract class BaseFilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val coverView: AppCompatImageView = itemView.findViewById(R.id.cover)
    private val titleView: TextView = itemView.findViewById(R.id.title)
    val detailsBtn: Button = itemView.findViewById(R.id.detailsBtn)
    val toggleFavouritesView: ImageView = itemView.findViewById(R.id.toggleFavourites)
    val removeFromFavouritesView: ImageView = itemView.findViewById(R.id.removeFromFavourites)
    val editSchedule: ImageView = itemView.findViewById(R.id.editSchedule)

    open fun bind(
        title: String,
        posterPath: String?,
        visited: Boolean,
        inFavourites: Boolean,
        scheduled: Boolean
    ) {
        titleView.text = title
        titleView.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                if (visited) R.color.visited else R.color.title
            )
        )

        if (scheduled) {
            editSchedule.setImageResource(R.drawable.ic_alarm)
            editSchedule.alpha = ON_ICON_ALPHA
        } else {
            editSchedule.setImageResource(R.drawable.ic_alarm_add)
            editSchedule.alpha = OFF_ICON_ALPHA
        }

        Glide.with(coverView.context)
            .load(posterPath)
            .placeholder(R.drawable.ic_no_photo)
            .into(coverView)
    }

    companion object {
        const val ON_ICON_ALPHA = 1f
        const val OFF_ICON_ALPHA = 0.4f
    }
}