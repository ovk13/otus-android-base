package ru.ovk13.otusandroidbase.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "films",
    primaryKeys = ["id", "type"],
    indices = [
        Index("type")
    ]
)
data class FilmDataModel(
    @ColumnInfo(name = "type")
    var type: String,
    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    val adult: Boolean,
    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String,
//    @SerializedName("genre_ids")
//    @ColumnInfo(name = "genre_ids")
//    val genreIds: List<Int>,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,
    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    val originalLanguage: String,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,
    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    val voteCount: Int,
    @SerializedName("video")
    @ColumnInfo(name = "video")
    val video: Boolean,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String?
) : Parcelable {
    @Ignore
    var visited: Boolean = false

    @Ignore
    var inFavourites: Boolean = false

    fun getAbsolutePosterPath(): String {
        return "https://image.tmdb.org/t/p/w500" + posterPath
    }

    companion object {
        const val VISITED = "visited"
        const val IN_FAVOURITES = "inFavourites"
        const val TYPE_FILM = "film"
        const val TYPE_FAVOURITE = "favourite"
        const val TYPE_TO_SEE = "to_see"
    }
}