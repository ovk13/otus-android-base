package ru.ovk13.otusandroidbase.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.Film
import ru.ovk13.otusandroidbase.ui.viewholders.FavouriteFilmViewHolder
import ru.ovk13.otusandroidbase.ui.viewholders.FilmViewHolder
import ru.ovk13.otusandroidbase.ui.viewholders.FooterViewHolder
import ru.ovk13.otusandroidbase.ui.viewholders.HeaderViewHolder

class FilmViewAdapter(
    private val inflater: LayoutInflater,
    private val title: String,
    private val type: Int,
    var itemsList: MutableList<Film?>,
    private val detailsClickListener: ((filmItem: Film) -> Unit)?,
    private val favouritesToggleClickListener: ((filmItem: Film, position: Int) -> Unit)?,
    private val favouritesDeleteClickListener: ((filmItem: Film, recyclerPosition: Int) -> Unit)?
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
                FilmViewHolder(
                    inflater.inflate(
                        R.layout.film_item,
                        parent,
                        false
                    )
                )
            } else {
                FavouriteFilmViewHolder(
                    inflater.inflate(R.layout.film_item, parent, false)
                )
            }
        } else if (viewType == VIEW_TYPE_HEADER) {
            HeaderViewHolder(
                inflater.inflate(
                    R.layout.header_item,
                    parent,
                    false
                ) as TextView
            )
        } else {
            FooterViewHolder(inflater.inflate(R.layout.footer_item, parent, false) as ProgressBar)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemsListPosition = if (title.isNotEmpty()) position - 1 else position
        return if (title.isNotEmpty() && position == 0) VIEW_TYPE_HEADER
        else if (itemsList[itemsListPosition] == null) VIEW_TYPE_FOOTER
        else VIEW_TYPE_ITEM
    }

    override fun getItemCount() =
        if (title.isEmpty()) itemsList.size else itemsList.size + itemsOffset // Добавляем header

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(title)
        } else if (holder is FooterViewHolder) {

        } else {
            val itemPosition = if (title.isEmpty()) position else position - itemsOffset
            val item = itemsList[itemPosition]

            if (holder is FilmViewHolder) {
                bindFilmViewHolder(holder, item as Film)
            } else if (holder is FavouriteFilmViewHolder) {
                bindFavoriteFilmViewHolder(holder, item as Film)
            }
        }
    }

    private fun bindFilmViewHolder(holder: FilmViewHolder, filmItem: Film) {
        holder.bind(
            filmItem.title,
            filmItem.posterPath,
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

    private fun bindFavoriteFilmViewHolder(holder: FavouriteFilmViewHolder, filmItem: Film) {
        holder.bind(
            filmItem.title,
            filmItem.posterPath,
            filmItem.visited
        )
        holder.removeFromFavourites.setOnClickListener {
            favouritesDeleteClickListener?.invoke(
                filmItem,
                holder.adapterPosition
            )
        }
        holder.detailsBtn.setOnClickListener {
            detailsClickListener?.invoke(filmItem)
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_FOOTER = 2
        const val TYPE_LIST = 0
        const val TYPE_FAVOURITES = 1
    }
}