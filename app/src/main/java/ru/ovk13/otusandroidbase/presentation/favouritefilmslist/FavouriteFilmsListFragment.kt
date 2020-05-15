package ru.ovk13.otusandroidbase.presentation.favouritefilmslist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_films_list.view.*
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListFragment
import ru.ovk13.otusandroidbase.presentation.filmdetail.FilmDetailFragment.Companion.FILM_ITEM
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

//    private fun initButtons(listType: Int, view: View, recyclerView: RecyclerView) {
//
//        val addBtn = view.findViewById<ImageButton>(R.id.addBtn)
//        val inviteBtn = view.findViewById<ImageButton>(R.id.inviteBtn)
//
//        if (listType == FilmViewAdapter.TYPE_FAVOURITES) {
////            initAddButton(addBtn, recyclerView)
////            showButton(addBtn)
//            hideButton(inviteBtn)
//        } else {
//            initInviteButton(inviteBtn)
//            showButton(inviteBtn)
//            hideButton(addBtn)
//        }
//
//    }
//
//
//    private fun initAddButton(button: ImageButton, recyclerView: RecyclerView) {
//        // Заглушка. Просдо добавляет новый фильм в список избранного
//        button.setOnClickListener {
//            listener?.onAddToFavouritesClick(1, recyclerView)
//        }
//    }

    override fun onDetailsClick(filmItem: FilmDataModel, position: Int) {
        // todo: передавать id и дергать фильм из room
        val bundle = bundleOf(FILM_ITEM to filmItem)
        filmItem.visited = true
        findNavController().navigate(
            R.id.action_favouriteFilmsListFragment_to_filmDetailFragment2,
            bundle
        )
    }

    override fun onRemoveFromFavouritesClick(
        filmItem: FilmDataModel,
        position: Int
    ) {
        removeFromFavourites(filmItem, position, FilmViewAdapter.TYPE_FAVOURITES)
    }
}