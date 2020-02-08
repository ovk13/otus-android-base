package ru.ovk13.otusandroidbase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilmViewAdapter(val inflater: LayoutInflater, val items: List<FilmItem>, val clickListener: FilmClickListener): RecyclerView.Adapter<FilmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(inflater.inflate(R.layout.film_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(items[position].coverDrawable, items[position].titleText, clickListener)
    }

    interface FilmClickListener {
        fun onFilmClick(position: Int)
    }
}