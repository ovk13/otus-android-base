package ru.ovk13.otusandroidbase.presentation.ui.viewholders

import android.view.View

class FavouriteFilmViewHolder(itemView: View) : BaseFilmViewHolder(itemView) {

    override fun bind(
        title: String,
        posterPath: String?,
        visited: Boolean,
        inFavourites: Boolean,
        scheduled: Boolean
    ) {
        super.bind(title, posterPath, visited, inFavourites, scheduled)

        toggleFavouritesView.visibility = View.GONE
        removeFromFavouritesView.visibility = View.VISIBLE
    }
}