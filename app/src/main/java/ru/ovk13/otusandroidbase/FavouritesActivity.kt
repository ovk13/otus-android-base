package ru.ovk13.otusandroidbase

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ovk13.otusandroidbase.FilmViewAdapter.Companion.TYPE_FAVOURITES
import ru.ovk13.otusandroidbase.recyclerview.decorations.LineItemDecoration


class FavouritesActivity : AppCompatActivity() {

    private var visitedFilms: MutableList<Int> = mutableListOf()
    private var favouriteFilms: MutableList<Int> = mutableListOf()
    private var favouriteFilmsList: MutableList<FilmItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        initDayNightSwitcher()
//        getDataFromState(savedInstanceState)
        getDataFromIntent()
        initFilmsRecycler()
    }

    override fun onBackPressed() {

        val intent = Intent()
        intent.putExtra(MainActivity.VISITED_FILMS, visitedFilms.toIntArray())
        intent.putExtra(MainActivity.FAVOURITE_FILMS, favouriteFilms.toIntArray())
        intent.putExtra(MainActivity.NIGHT_MODE, AppCompatDelegate.getDefaultNightMode())
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }

    private fun getDataFromState(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            visitedFilms =
                this.getIntArray(MainActivity.VISITED_FILMS)?.toMutableList() ?: mutableListOf()
            favouriteFilms =
                this.getIntArray(MainActivity.FAVOURITE_FILMS)?.toMutableList() ?: mutableListOf()
        }

        visitedFilms.forEach { pos: Int ->
            FilmsList.items[pos].visited = true
        }
        favouriteFilms.forEach { pos: Int ->
            FilmsList.items[pos].inFavourites = true
            favouriteFilmsList.add(FilmsList.items[pos])
        }
    }

    private fun getDataFromIntent() {
        visitedFilms =
            intent.getIntArrayExtra(MainActivity.VISITED_FILMS)?.toMutableList() ?: mutableListOf()
        favouriteFilms = intent.getIntArrayExtra(MainActivity.FAVOURITE_FILMS)?.toMutableList()
            ?: mutableListOf()

        visitedFilms.forEach { pos: Int ->
            FilmsList.items[pos].visited = true
        }
        favouriteFilms.forEach { pos: Int ->
            FilmsList.items[pos].inFavourites = true
            favouriteFilmsList.add(FilmsList.items[pos])
        }
    }

    private fun initDayNightSwitcher() {
        val dayNightSwitcher = findViewById<Switch>(R.id.dayNightSwitcher)
        dayNightSwitcher.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        dayNightSwitcher?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initFilmsRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.filmsRecycler)
        val layoutManager: RecyclerView.LayoutManager
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 2 else 1
                }
            }
        } else {
            layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FilmViewAdapter(
            LayoutInflater.from(this),
            getString(R.string.favourites),
            TYPE_FAVOURITES,
            favouriteFilmsList,
            object : FilmViewAdapter.FilmClickListener {
                override fun onDetailsClick(position: Int, dataPosition: Int) {
                    Log.d("POSIT", position.toString())
                    markVisited(position, dataPosition, recyclerView)

                    val intent = Intent(this@FavouritesActivity, DetailsActivity::class.java)
                    intent.putExtra(MainActivity.FILM_DATA, favouriteFilmsList[dataPosition])
                    startActivity(intent)
                }

                override fun onDeleteClick(position: Int, dataPosition: Int) {
                    if (dataPosition < favouriteFilms.size) {
                        favouriteFilms.removeAt(dataPosition)
                    }
                    favouriteFilmsList.removeAt(dataPosition)
                    recyclerView.adapter?.notifyItemRemoved(position)
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

        // Заглушка. Просдо добавляет новый фильм в список избранного
        findViewById<ImageButton>(R.id.addBtn)?.setOnClickListener {
            favouriteFilmsList.add(
                FilmItem(
                    R.string.film1Name,
                    R.drawable.film1,
                    R.string.film1Description,
                    false,
                    true
                )
            )
            recyclerView.adapter?.notifyItemInserted(favouriteFilmsList.size)
        }
    }

    private fun markVisited(position: Int, dataPosition: Int, recyclerView: RecyclerView) {
        favouriteFilmsList[dataPosition].visited = true
        recyclerView.adapter?.notifyItemChanged(position)

        if (!visitedFilms.contains(favouriteFilms[dataPosition])) {
            visitedFilms.add(favouriteFilms[dataPosition])
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(MainActivity.VISITED_FILMS, visitedFilms.toIntArray())
        outState.putIntArray(MainActivity.FAVOURITE_FILMS, favouriteFilms.toIntArray())
    }
}
