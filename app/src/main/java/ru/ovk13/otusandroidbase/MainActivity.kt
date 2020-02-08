package ru.ovk13.otusandroidbase

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var filmsViews: Map<Int, Map<String, View>>
    private lateinit var filmsData: Map<Int, FilmData>
    private var visitedFilms: MutableSet<Int> = mutableSetOf()
    private lateinit var filmsList: List<FilmItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filmsList = arrayListOf<FilmItem>(
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film1), resources.getString(R.string.film1Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film2), resources.getString(R.string.film2Name)),
            FilmItem(ContextCompat.getDrawable(this, R.drawable.film3), resources.getString(R.string.film3Name))
        )

        initFilmsRecycler()

        savedInstanceState?.apply {
            visitedFilms = this.getIntArray(VISITED_FILMS)?.toMutableSet() ?: mutableSetOf()
        }

//        filmsViews = mapOf(
//            1 to mapOf(
//                NAME_VIEW to findViewById<TextView>(R.id.film1Name) as View,
//                BUTTON to findViewById<Button>(R.id.film1DetailsBtn) as View
//            ),
//            2 to mapOf(
//                NAME_VIEW to findViewById<TextView>(R.id.film2Name) as View,
//                BUTTON to findViewById<Button>(R.id.film2DetailsBtn) as View
//            ),
//            3 to mapOf(
//                NAME_VIEW to findViewById<TextView>(R.id.film3Name) as View,
//                BUTTON to findViewById<Button>(R.id.film3DetailsBtn) as View
//            )
//        )
//
//        filmsData = mapOf(
//                1 to FilmData(R.string.film1Name, R.drawable.film1, R.string.film1Description),
//        2 to FilmData(R.string.film2Name, R.drawable.film2, R.string.film2Description),
//        3 to FilmData(R.string.film3Name, R.drawable.film3, R.string.film3Description)
//        )
//
//        for ((id, film) in filmsViews) {
//
//            if (visitedFilms.contains(id)) {
//                markVisited(id)
//            }
//
//            film[BUTTON]?.setOnClickListener(object : )
//        }

        findViewById<ImageButton>(R.id.inviteBtn)?.setOnClickListener { inviteFriend() }

        findViewById<Switch>(R.id.dayNightSwitcher)?.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        })
    }

    private fun initFilmsRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.filmsRecycler)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FilmViewAdapter(LayoutInflater.from(this), filmsList, object: FilmViewAdapter.FilmClickListener{
            override fun onFilmClick(position: Int) {
                Log.d("FilmPosition", position.toString())
            }

        })
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

    private fun markVisited(id: Int) {
        Log.d("visitedCOlor", R.color.visited.toString())
        (filmsViews[id]?.get(NAME_VIEW) as TextView).setTextColor(
            ContextCompat.getColor(
                this,
                R.color.visited
            )
        )

        if (!visitedFilms.contains(id)) {
            visitedFilms.add(id)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(VISITED_FILMS, visitedFilms.toIntArray())
        Log.i("save", visitedFilms.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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
        const val NAME_VIEW = "name_view"
        const val BUTTON = "button"
        const val FILM_DATA = "name_value"
        const val VISITED_FILMS = "visited_films"
        const val LIKE = "like"
        const val COMMENT = "comment"
        const val DETAIL_REQUEST_CODE = 13
        const val LIKE_TAG = "LikeResult"
    }
}
