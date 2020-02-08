package ru.ovk13.otusandroidbase

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmData(val nameResId: Int, val coverResId: Int, val descriptionResId: Int) : Parcelable