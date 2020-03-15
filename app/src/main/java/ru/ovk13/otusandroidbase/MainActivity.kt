package ru.ovk13.otusandroidbase

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.ovk13.otusandroidbase.data.Film
import ru.ovk13.otusandroidbase.data.Film.Companion.IN_FAVOURITES
import ru.ovk13.otusandroidbase.data.Film.Companion.VISITED
import ru.ovk13.otusandroidbase.data.FilmsResponse
import ru.ovk13.otusandroidbase.network.FilmsRepo
import ru.ovk13.otusandroidbase.ui.adapters.FilmViewAdapter
import ru.ovk13.otusandroidbase.ui.adapters.FilmViewAdapter.Companion.TYPE_FAVOURITES
import ru.ovk13.otusandroidbase.ui.adapters.FilmViewAdapter.Companion.TYPE_LIST
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity(), FilmsListFragment.FilmListListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    var filmsList: MutableList<Film?>? = null
    var currentPage: Int = 1
    var filmsTotalPages: Int = 0
    var isLoading = false
    private var visitedFilmsIds: MutableList<Int> = mutableListOf()
    var favouriteFilmsIds: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("EVENT", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDataFromState(savedInstanceState)

        if (filmsList === null) {
            filmsList = mutableListOf()
            loadFullFilmsList()
        }


        val mainNavigationView = findViewById<BottomNavigationView>(R.id.mainNavigation)
        mainNavigationView.setOnNavigationItemSelectedListener(this)

    }

    private fun loadFullFilmsList() {
        showLoader()
        FilmsRepo.loadItemsPage(object : FilmsRepo.LoadDataCallback {
            override fun onSuccess(data: FilmsResponse) {
                hideLoader()
                Log.d("FAV", favouriteFilmsIds.joinToString())

                filmsList = data.results
                currentPage = data.page
                filmsTotalPages = data.totalPages
                updateProperties(visitedFilmsIds, VISITED)
                updateProperties(favouriteFilmsIds, IN_FAVOURITES)

                showDefaultFragment()
            }

            override fun onError(error: String) {
                hideLoader()
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG)
                    .show()
                showDefaultFragment()
            }

            override fun onError(t: Throwable) {
                hideLoader()
                if (t is SocketTimeoutException) {
                    Toast.makeText(this@MainActivity, R.string.requestTimeout, Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                }
                showDefaultFragment()
            }

        })
    }

    private fun showDefaultFragment() {
        val listFragment =
            FilmsListFragment.newInstance(TYPE_LIST)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, listFragment, FilmsListFragment.TAG)
            .commit()
    }

    private fun showLoader() {
        loader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loader.visibility = View.GONE
    }

    private fun getDataFromState(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            visitedFilmsIds = this.getIntArray(VISITED_FILMS)?.toMutableList() ?: mutableListOf()
            favouriteFilmsIds = this.getIntArray(FAVOURITE_FILMS)?.toMutableList()
                ?: mutableListOf()
        }
    }

    private fun updateProperties(listOfMarked: MutableList<Int>, property: String) {
        filmsList?.forEach {
            when (property) {
                VISITED -> it?.visited = listOfMarked.contains(it?.id)
                IN_FAVOURITES -> it?.inFavourites = listOfMarked.contains(it?.id)
            }
        }
    }

    private fun markVisited(filmItem: Film) {
        filmItem.visited = true

        if (!visitedFilmsIds.contains(filmItem.id)) {
            visitedFilmsIds.add(filmItem.id)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(VISITED_FILMS, visitedFilmsIds.toIntArray())
        outState.putIntArray(FAVOURITE_FILMS, favouriteFilmsIds.toIntArray())
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val okListener = DialogInterface.OnClickListener { _, _ -> super.onBackPressed() }
            val cancelListener = DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }

            dialogBuilder.setMessage(R.string.exitDialogMessage)
                .setTitle(R.string.exitDialogTitle)
                .setNegativeButton(R.string.exitDialogNo, cancelListener)
                .setPositiveButton(R.string.exitDialogYes, okListener)

            val dialog: AlertDialog = dialogBuilder.create()
            dialog.show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.mainNavigationHome -> {
                val fragment =
                    FilmsListFragment.newInstance(TYPE_LIST)
                clearBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, FilmsListFragment.TAG)
                    .commit()

                return true
            }
            R.id.mainNavigationFavourites -> {
                val fragment =
                    FilmsListFragment.newInstance(TYPE_FAVOURITES)
                clearBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, "FAV")
                    .commit()

                return true
            }
            R.id.mainNavigationSettings -> {
                clearBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, SettingsFragment(), SettingsFragment.TAG)
                    .commit()

                return true
            }
        }

        return false
    }

    private fun clearBackStack() {
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun openFilmDetails(filmItem: Film) {
        val detailsFragment = FilmsDetailsFragment.newInstance(filmItem)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, detailsFragment, FilmsDetailsFragment.TAG)
            .addToBackStack(FilmsDetailsFragment.TAG)
            .commit()
    }

    override fun onDetailsClick(filmItem: Film) {
        openFilmDetails(filmItem)
        markVisited(filmItem)
    }

    override fun onToggleFavouritesClick(
        filmItem: Film,
        recyclerView: RecyclerView,
        position: Int
    ) {
        Log.d("position", position.toString())
        if (favouriteFilmsIds.contains(filmItem.id)) {

            val idPosition = favouriteFilmsIds.indexOf(filmItem.id)
            favouriteFilmsIds.remove(filmItem.id)
            filmItem.inFavourites = false
            recyclerView.adapter?.notifyItemChanged(position)

            showRemoveFromFavouritesSnackBar(View.OnClickListener {
                favouriteFilmsIds.add(idPosition, filmItem.id)
                filmItem.inFavourites = true
                recyclerView.adapter?.notifyItemChanged(position)
            })

        } else {

            favouriteFilmsIds.add(filmItem.id)
            filmItem.inFavourites = true
            recyclerView.adapter?.notifyItemChanged(position)

            showAddToFavouritesSnackBar(View.OnClickListener {
                favouriteFilmsIds.removeAt(favouriteFilmsIds.lastIndex)
                filmItem.inFavourites = false
                recyclerView.adapter?.notifyItemChanged(position)
            })

        }
    }

    override fun onRemoveFromFavouritesClick(
        filmItem: Film,
        recyclerView: RecyclerView,
        recyclerPosition: Int
    ) {
        Log.d("REMOVE_CLICK id", filmItem.id.toString())
        Log.d("REMOVE_CLICK fav", favouriteFilmsIds.joinToString())
        if (!favouriteFilmsIds.contains(filmItem.id)) {
            return
        }

        val adapter = recyclerView.adapter as FilmViewAdapter
        val favouritesPosition = adapter.itemsList.indexOf(filmItem)
        Log.d("REMOVE_CLICK array pos", favouritesPosition.toString())
        Log.d("REMOVE_CLICK RV pos", recyclerPosition.toString())

        favouriteFilmsIds.remove(filmItem.id)
        adapter.itemsList.remove(filmItem)
        filmItem.inFavourites = false
        adapter.notifyItemRemoved(recyclerPosition)

        showRemoveFromFavouritesSnackBar(View.OnClickListener {
            favouriteFilmsIds.add(favouritesPosition, filmItem.id)
            adapter.itemsList.add(favouritesPosition, filmItem)
            filmItem.inFavourites = true
            adapter.notifyItemInserted(recyclerPosition)
        })

    }

    /**
     * Добавление в избранное на странице избранного
     */
    override fun onAddToFavouritesClick(id: Int, recyclerView: RecyclerView) {
//        val adapter = recyclerView.adapter as FilmViewAdapter
//        favouriteFilmsIds.add(id)
//        adapter.itemsIds.add(id)
//        val itemPosition = adapter.itemCount - 1
//        adapter.notifyItemInserted(itemPosition)
//        showAddToFavouritesSnackBar(View.OnClickListener {
//            favouriteFilmsIds.removeAt(adapter.itemsIds.lastIndex)
//            adapter.itemsIds.removeAt(adapter.itemsIds.lastIndex)
//            adapter.notifyItemRemoved(itemPosition)
//        })
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
        Snackbar.make(findViewById(R.id.filmsListLayout), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undoFavouritesAction), clickListener)
            .setActionTextColor(
                ContextCompat.getColor(this, R.color.snackBarAction)
            )
            .show()
    }

    override fun onListScroll(recyclerView: RecyclerView) {
        Log.d("NEW_PAGE", currentPage.toString())
        Log.d("NEW_PAGE", filmsTotalPages.toString())
        Log.d(
            "NEW_PAGE",
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                .toString()
        )
        Log.d("NEW_PAGE", recyclerView.adapter?.itemCount.toString())
        if (!isLoading && (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1 == recyclerView.adapter?.itemCount) {

            val nextPage = currentPage + 1
            if (nextPage > filmsTotalPages) {
                return
            }
            Log.d("NEW_PAGE", nextPage.toString())
            Log.d("NEW_PAGE", "new page loading")

            val adapter = (recyclerView.adapter as FilmViewAdapter)
            showRecyclerLoader(adapter)

            isLoading = true

            FilmsRepo.loadItemsPage(object : FilmsRepo.LoadDataCallback {
                override fun onSuccess(data: FilmsResponse) {
                    hideRecyclerLoader(adapter)
                    isLoading = false
                    val startRangePosition = adapter.itemCount
                    val endRangePosition = startRangePosition + data.results.size - 1
                    filmsList?.addAll(data.results)
                    currentPage = data.page

                    adapter.notifyItemRangeInserted(startRangePosition, endRangePosition)
                }

                override fun onError(error: String) {
                    hideRecyclerLoader(adapter)
                    isLoading = false
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG)
                        .show()
                }

                override fun onError(t: Throwable) {
                    hideRecyclerLoader(adapter)
                    isLoading = false
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                                this@MainActivity,
                                R.string.requestTimeout,
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                }

            }, nextPage)
        }
    }

    private fun showRecyclerLoader(adapter: FilmViewAdapter) {
        adapter.itemsList.add(null)
        adapter.notifyItemInserted(adapter.itemCount - 1)
    }

    private fun hideRecyclerLoader(adapter: FilmViewAdapter) {
        if (!adapter.itemsList.contains(null)) {
            return
        }
        adapter.itemsList.remove(null)
        adapter.notifyItemRemoved(adapter.itemCount)
    }

    override fun onRefresh(pullToRefresh: SwipeRefreshLayout, adapter: FilmViewAdapter) {
        pullToRefresh.isRefreshing = false
        filmsList?.clear()
        adapter.notifyDataSetChanged()
        loadFullFilmsList()
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val VISITED_FILMS = "visited_films"
        const val FAVOURITE_FILMS = "favourite_films"
    }
}
