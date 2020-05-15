package ru.ovk13.otusandroidbase.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.presentation.ui.viewholders.FavouriteFilmViewHolder
import ru.ovk13.otusandroidbase.presentation.ui.viewholders.FilmViewHolder
import ru.ovk13.otusandroidbase.presentation.ui.viewholders.FooterViewHolder
import ru.ovk13.otusandroidbase.presentation.ui.viewholders.HeaderViewHolder

class FilmViewAdapter(
    private val inflater: LayoutInflater,
    private val title: String,
    private val type: Int,
    private val listener: FilmListListener
//    private val detailsClickListener: ((filmItem: FilmDataModel) -> Unit)?,
//    private val favouritesToggleClickListener: ((filmItem: FilmDataModel, position: Int) -> Unit)?,
//    private val favouritesDeleteClickListener: ((filmItem: FilmDataModel, recyclerPosition: Int) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemsList: MutableList<FilmDataModel?> = mutableListOf()

    // Отступ на header
    private val itemsOffset = if (title.isEmpty()) 0 else 1
    private lateinit var mRecyclerView: RecyclerView

    fun setItems(items: MutableList<FilmDataModel?>, callback: () -> Unit) {
        itemsList.clear()
        itemsList.addAll(items)

        notifyDataSetChanged()
        callback.invoke()
    }

    fun clearItems() {
        itemsList.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: FilmDataModel?) {
        itemsList.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun removeItem(item: FilmDataModel?) {
        if (!itemsList.contains(item)) {
            return
        }
        itemsList.remove(item)
        notifyItemRemoved(itemCount)
    }

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
                bindFilmViewHolder(holder, item as FilmDataModel)
            } else if (holder is FavouriteFilmViewHolder) {
                bindFavoriteFilmViewHolder(holder, item as FilmDataModel)
            }
        }
    }

    private fun bindFilmViewHolder(holder: FilmViewHolder, filmItem: FilmDataModel) {
        holder.bind(
            filmItem.title,
            filmItem.getAbsolutePosterPath(),
            filmItem.visited,
            filmItem.inFavourites,
            filmItem.scheduled
        )
        holder.toggleFavouritesView.setOnClickListener {
            listener.onToggleFavouritesClick(filmItem, holder.adapterPosition - itemsOffset)
        }
        holder.editSchedule.setOnClickListener {
            listener.onEditScheduleClick(filmItem, holder.adapterPosition - itemsOffset)
        }
        holder.detailsBtn.setOnClickListener {
            listener.onDetailsClick(filmItem, holder.adapterPosition - itemsOffset)
        }
    }

    private fun bindFavoriteFilmViewHolder(
        holder: FavouriteFilmViewHolder,
        filmItem: FilmDataModel
    ) {
        holder.bind(
            filmItem.title,
            filmItem.getAbsolutePosterPath(),
            filmItem.visited,
            filmItem.inFavourites,
            filmItem.scheduled
        )
        holder.removeFromFavouritesView.setOnClickListener {
            listener.onRemoveFromFavouritesClick(filmItem, holder.adapterPosition)
        }
        holder.editSchedule.setOnClickListener {
            listener.onEditScheduleClick(filmItem, holder.adapterPosition - itemsOffset)
        }
        holder.detailsBtn.setOnClickListener {
            listener.onDetailsClick(filmItem, holder.adapterPosition - itemsOffset)
        }
    }


    interface FilmListListener {
        fun onDetailsClick(
            filmItem: FilmDataModel,
            position: Int
        )

        fun onToggleFavouritesClick(
            filmItem: FilmDataModel,
            position: Int
        ) {
        }

        fun onRemoveFromFavouritesClick(
            filmItem: FilmDataModel,
            position: Int
        ) {
        }

        fun onAddToFavouritesClick(
            id: Int,
            recyclerView: RecyclerView
        ) {
        }

        fun onEditScheduleClick(
            filmItem: FilmDataModel,
            position: Int
        )

        fun onListScroll(
            recyclerView: RecyclerView
        ) {
        }

        fun onRefresh(
            pullToRefresh: SwipeRefreshLayout,
            adapter: FilmViewAdapter
        ) {
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