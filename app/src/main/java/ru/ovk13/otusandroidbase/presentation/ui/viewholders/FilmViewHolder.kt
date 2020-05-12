package ru.ovk13.otusandroidbase.presentation.ui.viewholders

import android.view.View

class FilmViewHolder(itemView: View) : BaseFilmViewHolder(itemView) {

    override fun bind(
        title: String,
        posterPath: String?,
        visited: Boolean,
        inFavourites: Boolean,
        scheduled: Boolean
    ) {
        super.bind(title, posterPath, visited, inFavourites, scheduled)

        toggleFavouritesView.visibility = View.VISIBLE
        removeFromFavouritesView.visibility = View.GONE

        if (inFavourites) {
            toggleFavouritesView.alpha = ON_ICON_ALPHA
        } else {
            toggleFavouritesView.alpha = OFF_ICON_ALPHA
        }
    }

    companion object {
        const val ON_ICON_ALPHA = 1f
        const val OFF_ICON_ALPHA = 0.4f
    }
}