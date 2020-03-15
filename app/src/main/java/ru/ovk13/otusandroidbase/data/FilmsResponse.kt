package ru.ovk13.otusandroidbase.data

import com.google.gson.annotations.SerializedName

data class FilmsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: MutableList<Film?>
)