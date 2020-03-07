package ru.ovk13.otusandroidbase.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ovk13.otusandroidbase.FilmsList
import ru.ovk13.otusandroidbase.HeaderViewHolder
import ru.ovk13.otusandroidbase.R

class FilmViewAdapter(
    private val inflater: LayoutInflater,
    private val title: String,
    private val type: Int,
    var itemsIds: MutableList<Int>,
    private val detailsClickListener: ((filmItem: FilmItem) -> Unit)?,
    private val favouritesToggleClickListener: ((filmItem: FilmItem, position: Int) -> Unit)?,
    private val favouritesDeleteClickListener: ((filmItem: FilmItem, recyclerPosition: Int, favouritesPosition: Int) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Отступ на header
    private val itemsOffset = if (title.isEmpty()) 0 else 1
    private lateinit var mRecyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            if (type == TYPE_LIST) {
                FilmViewHolder(inflater.inflate(R.layout.film_item, parent, false))
            } else {
                FavouriteFilmViewHolder(inflater.inflate(R.layout.film_item, parent, false))
            }
        } else {
            HeaderViewHolder(inflater.inflate(R.layout.header_item, parent, false) as TextView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (title.isNotEmpty() && position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun getItemCount() =
        if (title.isEmpty()) itemsIds.size else itemsIds.size + itemsOffset // Добавляем header

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(title)
        } else {
            val itemPosition = if (title.isEmpty()) position else position - itemsOffset
            val item = FilmsList.items[itemsIds[itemPosition]] as FilmItem

            if (holder is FilmViewHolder) {
                bindFilmViewHolder(holder, item)
            } else if (holder is FavouriteFilmViewHolder) {
                bindFavoriteFilmViewHolder(holder, item)
            }
        }
    }

    private fun bindFilmViewHolder(holder: FilmViewHolder, filmItem: FilmItem) {
        holder.bind(
            filmItem.titleResId,
            filmItem.coverResId,
            filmItem.visited,
            filmItem.inFavourites
        )
        holder.toggleFavourites.setOnClickListener {
            favouritesToggleClickListener?.invoke(filmItem, holder.adapterPosition - itemsOffset)
        }
        holder.detailsBtn.setOnClickListener {
            detailsClickListener?.invoke(filmItem)
        }
    }

    private fun bindFavoriteFilmViewHolder(holder: FavouriteFilmViewHolder, filmItem: FilmItem) {
        holder.bind(
            filmItem.titleResId,
            filmItem.coverResId,
            filmItem.visited
        )
        holder.removeFromFavourites.setOnClickListener {
            favouritesDeleteClickListener?.invoke(
                filmItem,
                holder.adapterPosition,
                holder.adapterPosition - itemsOffset
            )
        }
        holder.detailsBtn.setOnClickListener {
            detailsClickListener?.invoke(filmItem)
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_HEADER = 1
        const val TYPE_LIST = 0
        const val TYPE_FAVOURITES = 1
    }
}