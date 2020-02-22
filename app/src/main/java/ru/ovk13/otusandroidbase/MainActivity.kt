package ru.ovk13.otusandroidbase

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ovk13.otusandroidbase.FilmItem.Companion.IN_FAVOURITES
import ru.ovk13.otusandroidbase.FilmItem.Companion.VISITED
import ru.ovk13.otusandroidbase.FilmViewAdapter.Companion.TYPE_LIST
import ru.ovk13.otusandroidbase.recyclerview.decorations.LineItemDecoration

class MainActivity : AppCompatActivity() {

    private var visitedFilms: MutableList<Int> = mutableListOf()
    private var favouriteFilms: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("EVENT", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDayNightSwitcher()
        getDataFromState(savedInstanceState)
        initFilmsRecycler()

        findViewById<ImageButton>(R.id.inviteBtn)?.setOnClickListener { inviteFriend() }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_favourites) {
            val intent = Intent(this, FavouritesActivity::class.java)
            intent.putExtra(VISITED_FILMS, visitedFilms.toIntArray())
            intent.putExtra(FAVOURITE_FILMS, favouriteFilms.toIntArray())
            startActivityForResult(intent, FAVOURITES_REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initDayNightSwitcher() {
        val dayNightSwitcher = findViewById<Switch>(R.id.dayNightSwitcher)
        dayNightSwitcher.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        Log.d("EVENT", dayNightSwitcher.isChecked.toString())
        dayNightSwitcher?.setOnCheckedChangeListener { _, isChecked ->
            Log.d("EVENT", isChecked.toString())
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun getDataFromState(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            visitedFilms = this.getIntArray(VISITED_FILMS)?.toMutableList() ?: mutableListOf()
            favouriteFilms = this.getIntArray(FAVOURITE_FILMS)?.toMutableList() ?: mutableListOf()
        }

        updateProperties(visitedFilms, VISITED)
        updateProperties(favouriteFilms, IN_FAVOURITES)
    }

    private fun updateProperties(listOfMarked: MutableList<Int>, property: String) {
        FilmsList.items.forEachIndexed { pos, film ->
            when (property) {
                VISITED -> film.visited = listOfMarked.contains(pos)
                IN_FAVOURITES -> film.inFavourites = listOfMarked.contains(pos)
            }
        }
    }

    private fun initFilmsRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.filmsRecycler)
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
            } else {
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FilmViewAdapter(
            LayoutInflater.from(this),
            "",
            TYPE_LIST,
            FilmsList.items,
            object : FilmViewAdapter.FilmClickListener {
                override fun onDetailsClick(position: Int, dataPosition: Int) {
                    markVisited(position, dataPosition, recyclerView)

                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtra(FILM_DATA, FilmsList.items[dataPosition])
                    startActivityForResult(intent, DETAIL_REQUEST_CODE)
                }

                override fun onFavouritesClick(position: Int, dataPosition: Int) {
                    if (favouriteFilms.contains(dataPosition)) {
                        favouriteFilms.remove(dataPosition)
                        FilmsList.items[dataPosition].inFavourites = false
                    } else {
                        favouriteFilms.add(dataPosition)
                        FilmsList.items[dataPosition].inFavourites = true
                    }
                    recyclerView.adapter?.notifyItemChanged(position)
                }
            })
        val decorator =
            LineItemDecoration(
                10,
                30,
                ContextCompat.getColor(this, R.color.filmDecorator),
                7f
            )
        recyclerView.addItemDecoration(decorator)
    }

    private fun inviteFriend() {
        val inviteIntent = Intent(Intent.ACTION_SEND)
        inviteIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inviteTitle))
        inviteIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.inviteText))
        inviteIntent.type = "text/plain"
        val inviteChooser =
            Intent.createChooser(inviteIntent, getString(R.string.inviteChooserTitle))
        inviteIntent.resolveActivity(packageManager)?.let {
            startActivity(inviteChooser)
        }
    }

    private fun markVisited(position: Int, dataPosition: Int, recyclerView: RecyclerView) {
        FilmsList.items[dataPosition].visited = true
        recyclerView.adapter?.notifyItemChanged(position)

        if (!visitedFilms.contains(dataPosition)) {
            visitedFilms.add(dataPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(VISITED_FILMS, visitedFilms.toIntArray())
        outState.putIntArray(FAVOURITE_FILMS, favouriteFilms.toIntArray())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.d("EVENT", "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DETAIL_REQUEST_CODE) {
            var like: Boolean? = null
            var comment: String? = null
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    like = it.getBooleanExtra(LIKE, false)
                    comment = it.getStringExtra(COMMENT)
                }
            }
            Log.i(LIKE_TAG, like.toString())
            Log.i(LIKE_TAG, comment ?: "")
        } else if (requestCode == FAVOURITES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                visitedFilms =
                    it.getIntArrayExtra(VISITED_FILMS)?.toMutableList() ?: mutableListOf()
                favouriteFilms =
                    it.getIntArrayExtra(FAVOURITE_FILMS)?.toMutableList() ?: mutableListOf()
                updateProperties(visitedFilms, VISITED)
                updateProperties(favouriteFilms, IN_FAVOURITES)
                val recyclerView = findViewById<RecyclerView>(R.id.filmsRecycler)
                recyclerView?.adapter?.notifyDataSetChanged()

                findViewById<Switch>(R.id.dayNightSwitcher).isChecked = it.getIntExtra(
                    NIGHT_MODE,
                    AppCompatDelegate.MODE_NIGHT_NO
                ) == AppCompatDelegate.MODE_NIGHT_YES
            }

        }
    }

    override fun onBackPressed() {

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

    companion object {
        const val FILM_DATA = "name_value"
        const val VISITED_FILMS = "visited_films"
        const val FAVOURITE_FILMS = "favourite_films"
        const val LIKE = "like"
        const val COMMENT = "comment"
        const val DETAIL_REQUEST_CODE = 13
        const val FAVOURITES_REQUEST_CODE = 14
        const val LIKE_TAG = "LikeResult"
        const val NIGHT_MODE = "night_mode"
    }
}
