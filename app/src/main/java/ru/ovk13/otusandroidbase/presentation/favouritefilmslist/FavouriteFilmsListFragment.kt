package ru.ovk13.otusandroidbase.presentation.favouritefilmslist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_films_list.view.*
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListFragment
import ru.ovk13.otusandroidbase.presentation.ui.adapters.FilmViewAdapter

class FavouriteFilmsListFragment : BaseFilmsListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesViewModel!!.films.observe(this.viewLifecycleOwner, Observer { films ->
            adapter!!.setItems(films.toMutableList()) {}
        })
    }

    override fun initRecyclerView() {
        initRecyclerByType(FilmViewAdapter.TYPE_FAVOURITES)

        view!!.pullToRefresh.isEnabled = false
    }

    override fun onRemoveFromFavouritesClick(
        filmItem: FilmDataModel,
        position: Int
    ) {
        removeFromFavourites(filmItem, position, FilmViewAdapter.TYPE_FAVOURITES)
    }
}