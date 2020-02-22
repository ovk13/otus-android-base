package ru.ovk13.otusandroidbase

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmViewAdapter(
    private val inflater: LayoutInflater,
    private val title: String,
    private val type: Int,
    private val items: List<FilmItem>,
    private val clickListener: FilmClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Отступ на header
    private val itemsOffset = if (title.isEmpty()) 0 else 1

    fun getItemsOffset(): Int {
        return itemsOffset
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            if (type == TYPE_LIST) {
                FilmViewHolder(inflater.inflate(R.layout.film_item, parent, false), itemsOffset)
            } else {
                FavouriteFilmViewHolder(
                    inflater.inflate(R.layout.film_item, parent, false),
                    itemsOffset
                )
            }
        } else {
            HeaderViewHolder(inflater.inflate(R.layout.header_item, parent, false) as TextView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (title.isNotEmpty() && position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun getItemCount() =
        if (title.isEmpty()) items.size else items.size + itemsOffset // Добавляем header

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(title)
        } else if (holder is FilmViewHolder) {
            val itemPosition = if (title.isEmpty()) position else position - itemsOffset
            holder.bind(
                items[itemPosition].titleResId,
                items[itemPosition].coverResId,
                items[itemPosition].visited,
                items[itemPosition].inFavourites,
                clickListener
            )
        } else if (holder is FavouriteFilmViewHolder) {
            val itemPosition = if (title.isEmpty()) position else position - itemsOffset
            holder.bind(
                items[itemPosition].titleResId,
                items[itemPosition].coverResId,
                items[itemPosition].visited,
                clickListener
            )
        }
    }

    interface FilmClickListener {
        fun onDetailsClick(position: Int, dataPosition: Int)
        fun onFavouritesClick(position: Int, dataPosition: Int) {}
        fun onDeleteClick(position: Int, dataPosition: Int) {}
    }


    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_HEADER = 1
        const val TYPE_LIST = 0
        const val TYPE_FAVOURITES = 1
    }
}