package ru.ovk13.otusandroidbase.presentation.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.presentation.favouritefilmslist.FavouriteFilmsListViewModel
import ru.ovk13.otusandroidbase.presentation.favouritefilmslist.FavouriteFilmsListViewModelFactory
import ru.ovk13.otusandroidbase.presentation.ui.adapters.FilmViewAdapter
import ru.ovk13.otusandroidbase.presentation.ui.decorations.LineItemDecoration

abstract class BaseFilmsListFragment : Fragment(), FilmViewAdapter.FilmListListener {

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

        favouritesViewModel =
            ViewModelProvider(activity!!, FavouriteFilmsListViewModelFactory()).get(
                FavouriteFilmsListViewModel::class.java
            )

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

//        initButtons(listType, view, recyclerView)
    }

    protected fun showButton(button: ImageButton) {
        button.visibility = View.VISIBLE
    }

    protected fun hideButton(button: ImageButton) {
        button.visibility = View.GONE
    }

//    private fun initInviteButton(button: ImageButton) {
//        button.setOnClickListener {
//            val inviteIntent = Intent(Intent.ACTION_SEND)
//            inviteIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inviteTitle))
//            inviteIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.inviteText))
//            inviteIntent.type = "text/plain"
//            val inviteChooser =
//                Intent.createChooser(inviteIntent, getString(R.string.inviteChooserTitle))
//            inviteIntent.resolveActivity((activity as AppCompatActivity).packageManager)?.let {
//                startActivity(inviteChooser)
//            }
//        }
//    }

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
            favouritesViewModel?.removeFilm(filmItem)
            filmItem.inFavourites = false
            adapter!!.notifyItemChanged(position)
        })
    }

    protected fun removeFromFavourites(
        filmItem: FilmDataModel,
        position: Int,
        type: Int
    ) {
        val index = favouritesViewModel?.removeFilm(filmItem)
        filmItem.inFavourites = false
        if (type == FilmViewAdapter.TYPE_FAVOURITES) {
            adapter!!.notifyItemRemoved(position)
        } else {
            adapter!!.notifyItemChanged(position)
        }

        showRemoveFromFavouritesSnackBar(View.OnClickListener {
            favouritesViewModel?.addFilm(filmItem, index)
            filmItem.inFavourites = true
            if (type == FilmViewAdapter.TYPE_FAVOURITES) {
                adapter!!.notifyItemInserted(position)
            } else {
                adapter!!.notifyItemChanged(position)
            }
        })
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

    companion object {
        const val TAG = "filmsListFragment"
        const val LIST_ITEMS = "filmsListItems"
        const val LIST_TYPE = "filmsListType"
    }
}