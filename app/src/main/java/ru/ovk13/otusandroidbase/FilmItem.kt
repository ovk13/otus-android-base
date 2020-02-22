package ru.ovk13.otusandroidbase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmItem(
    val titleResId: Int,
    val coverResId: Int,
    val descriptionResId: Int,
    var visited: Boolean = false,
    var inFavourites: Boolean = false
) : Parcelable {
    companion object {
        const val VISITED = "visited"
        const val IN_FAVOURITES = "inFavourites"
    }
}