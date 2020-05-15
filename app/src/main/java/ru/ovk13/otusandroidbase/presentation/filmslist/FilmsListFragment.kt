package ru.ovk13.otusandroidbase.presentation.filmslist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_films_list.*
import kotlinx.android.synthetic.main.fragment_films_list.view.*
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.LoadingErrorModel
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListFragment
import ru.ovk13.otusandroidbase.presentation.filmdetail.FilmDetailFragment.Companion.FILM_ITEM
import ru.ovk13.otusandroidbase.presentation.ui.adapters.FilmViewAdapter

class FilmsListFragment : BaseFilmsListFragment(), FilmViewAdapter.FilmListListener {

    private var isLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmsViewModel!!.films.observe(this.viewLifecycleOwner, Observer { films ->
            hideRecyclerLoader()
            loadFailedWrapper.visibility = View.GONE
            adapter!!.setItems(films.toMutableList()) {
                isLoading = false
            }
        })
        filmsViewModel!!.error.observe(this.viewLifecycleOwner, Observer { error ->

            if (error != null) {
                isLoading = false
                hideRecyclerLoader()
                if (error.type == LoadingErrorModel.FULL_RELOAD) {
                    loadFailedWrapper.visibility = View.VISIBLE
                    Toast.makeText(context, error.message, Toast.LENGTH_LONG)
                        .show()
                } else if (error.type == LoadingErrorModel.LOAD_PAGE) {
                    Snackbar.make(
                        view.findViewById(R.id.filmsListLayout),
                        getString(R.string.loadPageFailedText) + ". " + error.message,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(getString(R.string.tryAgainAction)) {
                            isLoading = true
                            showRecyclerLoader()
                            filmsViewModel!!.loadPage()
                        }
                        .setActionTextColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.snackBarAction
                            )
                        )
                        .show()
                }
                filmsViewModel!!.clearError()
            }
        })
        if (filmsViewModel!!.films.value.isNullOrEmpty()) {
            isLoading = true
            showRecyclerLoader()
            filmsViewModel!!.loadPage()
        }

        reloadButton.setOnClickListener {
            isLoading = true
            showRecyclerLoader()
            loadFailedWrapper.visibility = View.GONE
            reload()
        }

        initInviteButton()
    }

    override fun initRecyclerView() {
        initRecyclerByType(FilmViewAdapter.TYPE_LIST)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val recyclerItemsCount = recyclerView.adapter?.itemCount ?: 0
                if (!isLoading && recyclerItemsCount > 0 && (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1 == recyclerItemsCount) {

                    isLoading = true
                    showRecyclerLoader()
                    loadFailedWrapper.visibility = View.GONE
                    filmsViewModel!!.loadNextPage()
                }
            }
        })

        view!!.pullToRefresh.isEnabled = true
        view!!.pullToRefresh.setOnRefreshListener {
            view!!.pullToRefresh.isRefreshing = false
            reload()
        }
    }

    private fun reload() {
        isLoading = true
        adapter!!.clearItems()
        showRecyclerLoader()
        filmsViewModel!!.reloadAll()
    }

    private fun initInviteButton() {

        val inviteBtn = view!!.findViewById<ImageButton>(R.id.inviteBtn)
        showButton(inviteBtn)
        hideButton(addBtn)

        inviteBtn.setOnClickListener {
            val inviteIntent = Intent(Intent.ACTION_SEND)
            inviteIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inviteTitle))
            inviteIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.inviteText))
            inviteIntent.type = "text/plain"
            val inviteChooser =
                Intent.createChooser(inviteIntent, getString(R.string.inviteChooserTitle))
            inviteIntent.resolveActivity((activity as AppCompatActivity).packageManager)?.let {
                startActivity(inviteChooser)
            }
        }
    }

    override fun onDetailsClick(filmItem: FilmDataModel, position: Int) {
        // todo: передавать id и дергать фильм из room
        val bundle = bundleOf(FILM_ITEM to filmItem)
        filmsViewModel!!.addVisited(filmItem.id)
        findNavController().navigate(R.id.action_filmsListFragment_to_filmDetailFragment, bundle)
    }

    override fun onToggleFavouritesClick(
        filmItem: FilmDataModel,
        position: Int
    ) {
        if (favouritesViewModel!!.isInFavourites(filmItem.id)) {
            removeFromFavourites(filmItem, position, FilmViewAdapter.TYPE_LIST)
        } else {
            addToFavourites(filmItem, position)
        }
    }
}