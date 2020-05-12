package ru.ovk13.otusandroidbase.presentation.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.ovk13.otusandroidbase.FilmsApplication
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.presentation.favouritefilmslist.FavouriteFilmsListViewModel
import ru.ovk13.otusandroidbase.presentation.favouritefilmslist.FavouriteFilmsListViewModelFactory
import ru.ovk13.otusandroidbase.presentation.filmdetail.FilmDetailFragment
import ru.ovk13.otusandroidbase.presentation.filmslist.FilmsListViewModel
import ru.ovk13.otusandroidbase.presentation.filmslist.FilmsListViewModelFactory
import ru.ovk13.otusandroidbase.presentation.scheduleEditor.ScheduleEditorFragment
import ru.ovk13.otusandroidbase.presentation.ui.adapters.FilmViewAdapter
import ru.ovk13.otusandroidbase.presentation.ui.decorations.LineItemDecoration

abstract class BaseFilmsListFragment : Fragment(), FilmViewAdapter.FilmListListener {

    protected var filmsViewModel: FilmsListViewModel? = null
    protected var favouritesViewModel: FavouriteFilmsListViewModel? = null
    protected var recyclerView: RecyclerView? = null
    protected var adapter: FilmViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_films_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmsViewModel = ViewModelProvider(
            activity!!,
            FilmsListViewModelFactory(
                FilmsApplication.instance!!.filmsUseCase,
                FilmsApplication.instance!!.favouritesUseCase,
                FilmsApplication.instance!!.visitedUseCase,
                FilmsApplication.instance!!.scheduleUseCase
            )
        ).get(
            FilmsListViewModel::class.java
        )

        favouritesViewModel =
            ViewModelProvider(
                activity!!,
                FavouriteFilmsListViewModelFactory(
                    FilmsApplication.instance!!.favouritesUseCase,
                    FilmsApplication.instance!!.visitedUseCase,
                    FilmsApplication.instance!!.scheduleUseCase
                )
            ).get(
                FavouriteFilmsListViewModel::class.java
            )

        if (favouritesViewModel!!.films.value.isNullOrEmpty()) {
            favouritesViewModel!!.loadFavourites()
        }

        initRecyclerView()
    }

    abstract fun initRecyclerView()

    fun initRecyclerByType(type: Int) {

        recyclerView = view!!.findViewById<RecyclerView>(R.id.filmsRecycler)
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            } else {
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

        recyclerView!!.layoutManager = layoutManager
        adapter = FilmViewAdapter(
            LayoutInflater.from(context),
            if (type == FilmViewAdapter.TYPE_FAVOURITES) getString(R.string.favourites)
            else "",
            type,
            this
        )
        recyclerView!!.adapter = adapter
        val decorator = LineItemDecoration(
            10,
            30,
            ContextCompat.getColor(
                context!!,
                R.color.filmDecorator
            ),
            7f
        )
        recyclerView!!.addItemDecoration(decorator)

    }

    protected fun showButton(button: ImageButton) {
        button.visibility = View.VISIBLE
    }

    protected fun hideButton(button: ImageButton) {
        button.visibility = View.GONE
    }

    protected fun showRecyclerLoader() {
        adapter!!.addItem(null)
    }

    protected fun hideRecyclerLoader() {
        adapter!!.removeItem(null)
    }

    protected fun addToFavourites(
        filmItem: FilmDataModel,
        position: Int
    ) {
        favouritesViewModel?.addFilm(filmItem)
        filmItem.inFavourites = true
        adapter!!.notifyItemChanged(position)

        showAddToFavouritesSnackBar(View.OnClickListener {
            val index = MutableLiveData<Int?>()
            favouritesViewModel?.removeFilm(filmItem, index)
            filmItem.inFavourites = false
            adapter!!.notifyItemChanged(position)
        })
    }

    protected fun removeFromFavourites(
        filmItem: FilmDataModel,
        position: Int,
        type: Int
    ) {
        val indexLiveData = MutableLiveData<Int?>()
        favouritesViewModel?.removeFilm(filmItem, indexLiveData)
        filmsViewModel?.setFavouriteStatus(filmItem.id, false)
        indexLiveData.observe(this.viewLifecycleOwner, object : Observer<Int?> {
            override fun onChanged(index: Int?) {
                showRemoveFromFavouritesSnackBar(View.OnClickListener {
                    favouritesViewModel?.addFilm(filmItem, index)
                    filmsViewModel?.setFavouriteStatus(filmItem.id, true)
                    filmItem.inFavourites = true
                    if (type == FilmViewAdapter.TYPE_FAVOURITES) {
                        adapter!!.notifyItemInserted(position)
                    } else {
                        adapter!!.notifyItemChanged(position)
                    }
                })
                indexLiveData.removeObserver(this)
            }
        })
        filmItem.inFavourites = false
        if (type == FilmViewAdapter.TYPE_FAVOURITES) {
            adapter!!.notifyItemRemoved(position)
        } else {
            adapter!!.notifyItemChanged(position)
        }


    }

    private fun showAddToFavouritesSnackBar(actionClickListener: View.OnClickListener) {
        showFavouritesSnackBar(getString(R.string.completeAddToFavourites), actionClickListener)
    }

    private fun showRemoveFromFavouritesSnackBar(actionClickListener: View.OnClickListener) {
        showFavouritesSnackBar(
            getString(R.string.completeRemoveFromFavourites),
            actionClickListener
        )
    }

    private fun showFavouritesSnackBar(message: String, clickListener: View.OnClickListener) {
        Snackbar.make(view!!.findViewById(R.id.filmsListLayout), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undoFavouritesAction), clickListener)
            .setActionTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.snackBarAction
                )
            )
            .show()
    }

    override fun onEditScheduleClick(filmItem: FilmDataModel, position: Int) {
        val id = filmItem.id
        val bundle = bundleOf(ScheduleEditorFragment.ID to id)
        findNavController().navigate(R.id.action_open_scheduleEditorFragment, bundle)
    }

    override fun onDetailsClick(filmItem: FilmDataModel, position: Int) {
        // todo: передавать id и дергать фильм из room
        val bundle = bundleOf(FilmDetailFragment.ID to filmItem.id)
        filmsViewModel!!.addVisited(filmItem.id)
        findNavController().navigate(R.id.action_open_filmDetailFragment, bundle)
    }

    companion object {
        const val TAG = "filmsListFragment"
        const val LIST_ITEMS = "filmsListItems"
        const val LIST_TYPE = "filmsListType"
    }
}