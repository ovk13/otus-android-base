package ru.ovk13.otusandroidbase.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double
) : Parcelable {
    @SerializedName("poster_path")
    val posterPath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
    var visited: Boolean = false
    var inFavourites: Boolean = false

    companion object {
        const val VISITED = "visited"
        const val IN_FAVOURITES = "inFavourites"
    }
}