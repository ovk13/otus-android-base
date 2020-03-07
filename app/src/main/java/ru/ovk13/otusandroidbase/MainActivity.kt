package ru.ovk13.otusandroidbase

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ru.ovk13.otusandroidbase.recycler.FilmItem
import ru.ovk13.otusandroidbase.recycler.FilmItem.Companion.IN_FAVOURITES
import ru.ovk13.otusandroidbase.recycler.FilmItem.Companion.VISITED
import ru.ovk13.otusandroidbase.recycler.FilmViewAdapter
import ru.ovk13.otusandroidbase.recycler.FilmViewAdapter.Companion.TYPE_FAVOURITES
import ru.ovk13.otusandroidbase.recycler.FilmViewAdapter.Companion.TYPE_LIST

class MainActivity : AppCompatActivity(), FilmsListFragment.FilmClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var visitedFilmsIds: MutableList<Int> = mutableListOf()
    private var favouriteFilmsIds: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("EVENT", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val listFragment =
                FilmsListFragment.newInstance(FilmsList.items.keys.toIntArray(), TYPE_LIST)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, listFragment, FilmsListFragment.TAG)
                .commit()
        }
        getDataFromState(savedInstanceState)

        val mainNavigationView = findViewById<BottomNavigationView>(R.id.mainNavigation)
        mainNavigationView.setOnNavigationItemSelectedListener(this)

    }

    private fun getDataFromState(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            visitedFilmsIds = this.getIntArray(VISITED_FILMS)?.toMutableList() ?: mutableListOf()
            favouriteFilmsIds = this.getIntArray(FAVOURITE_FILMS)?.toMutableList()
                ?: mutableListOf()
        }

        updateProperties(visitedFilmsIds, VISITED)
        updateProperties(favouriteFilmsIds, IN_FAVOURITES)
    }

    private fun updateProperties(listOfMarked: MutableList<Int>, property: String) {
        FilmsList.items.forEach { (pos, film) ->
            when (property) {
                VISITED -> film.visited = listOfMarked.contains(pos)
                IN_FAVOURITES -> film.inFavourites = listOfMarked.contains(pos)
            }
        }
    }

    private fun markVisited(filmItem: FilmItem) {
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
                    FilmsListFragment.newInstance(FilmsList.items.keys.toIntArray(), TYPE_LIST)
                clearBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, FilmsListFragment.TAG)
                    .commit()

                return true
            }
            R.id.mainNavigationFavourites -> {
                val fragment =
                    FilmsListFragment.newInstance(favouriteFilmsIds.toIntArray(), TYPE_FAVOURITES)
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

    private fun openFilmDetails(filmItem: FilmItem) {
        val detailsFragment = FilmsDetailsFragment.newInstance(filmItem)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, detailsFragment, FilmsDetailsFragment.TAG)
            .addToBackStack(FilmsDetailsFragment.TAG)
            .commit()
    }

    override fun onDetailsClick(filmItem: FilmItem) {
        openFilmDetails(filmItem)
        markVisited(filmItem)
    }

    override fun onToggleFavouritesClick(
        filmItem: FilmItem,
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
        filmItem: FilmItem,
        recyclerView: RecyclerView,
        recyclerPosition: Int,
        favouritesPosition: Int
    ) {
        if (!favouriteFilmsIds.contains(filmItem.id)) {
            return
        }

        val adapter = recyclerView.adapter as FilmViewAdapter
        Log.d("array position", favouritesPosition.toString())
        Log.d("RV position", recyclerPosition.toString())

        favouriteFilmsIds.removeAt(favouritesPosition)
        adapter.itemsIds.removeAt(favouritesPosition)
        filmItem.inFavourites = false
        adapter.notifyItemRemoved(recyclerPosition)

        showRemoveFromFavouritesSnackBar(View.OnClickListener {
            favouriteFilmsIds.add(favouritesPosition, filmItem.id)
            adapter.itemsIds.add(favouritesPosition, filmItem.id)
            adapter.notifyItemInserted(recyclerPosition)
        })

    }

    /**
     * Добавление в избранное на странице избранного
     */
    override fun onAddToFavouritesClick(id: Int, recyclerView: RecyclerView) {
        val adapter = recyclerView.adapter as FilmViewAdapter
        favouriteFilmsIds.add(id)
        adapter.itemsIds.add(id)
        val itemPosition = adapter.itemCount - 1
        adapter.notifyItemInserted(itemPosition)
        showAddToFavouritesSnackBar(View.OnClickListener {
            favouriteFilmsIds.removeAt(adapter.itemsIds.lastIndex)
            adapter.itemsIds.removeAt(adapter.itemsIds.lastIndex)
            adapter.notifyItemRemoved(itemPosition)
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
        Snackbar.make(findViewById(R.id.filmsListLayout), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undoFavouritesAction), clickListener)
            .setActionTextColor(
                ContextCompat.getColor(this, R.color.snackBarAction)
            )
            .show()
    }

    companion object {
        const val VISITED_FILMS = "visited_films"
        const val FAVOURITE_FILMS = "favourite_films"
    }
}
