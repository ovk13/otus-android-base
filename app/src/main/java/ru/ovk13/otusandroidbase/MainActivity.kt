package ru.ovk13.otusandroidbase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity()  {

    private lateinit var filmsViews : Map<Int, Map<String, View>>
    private lateinit var filmsData : Map<Int, FilmData>
    private var visitedFilms : MutableSet<Int> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.apply {
            visitedFilms = this.getIntArray(VISITED_FILMS)?.toMutableSet() ?: mutableSetOf()
        }

        filmsViews = mapOf(
            1 to mapOf(
                NAME_VIEW to findViewById<TextView>(R.id.film1Name) as View,
                BUTTON to findViewById<Button>(R.id.film1DetailsBtn) as View
            ),
            2 to mapOf(
                NAME_VIEW to findViewById<TextView>(R.id.film2Name) as View,
                BUTTON to findViewById<Button>(R.id.film2DetailsBtn) as View
            ),
            3 to mapOf(
                NAME_VIEW to findViewById<TextView>(R.id.film3Name) as View,
                BUTTON to findViewById<Button>(R.id.film3DetailsBtn) as View
            )
        )

        filmsData = mapOf(
            1 to FilmData(R.string.film1Name, R.drawable.film1, R.string.film1Description),
            2 to FilmData(R.string.film2Name, R.drawable.film2, R.string.film2Description),
            3 to FilmData(R.string.film3Name, R.drawable.film3, R.string.film3Description)
        )

        for ((id, film) in filmsViews) {

            if (visitedFilms.contains(id)) {
                markVisited(id)
            }

            film[BUTTON]?.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    markVisited(id)
                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtra(FILM_DATA, filmsData[id])
                    intent.resolveActivity(packageManager)?.let {
                        startActivityForResult(intent, DETAIL_REQUEST_CODE)
                    }
                }

            })
        }

        findViewById<Button>(R.id.inviteBtn)?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                inviteFriend()
            }

        })
    }

    private fun inviteFriend() {
        val inviteIntent = Intent(Intent.ACTION_SEND)
        inviteIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inviteTitle));
        inviteIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.inviteText))
        inviteIntent.type = "text/plain"
        val inviteChooser = Intent.createChooser(inviteIntent, getString(R.string.inviteChooserTitle))
        inviteIntent.resolveActivity(packageManager)?.let {
            startActivity(inviteChooser)
        }
    }

    private fun markVisited(id: Int) {
        (filmsViews[id]?.get(NAME_VIEW) as TextView).setTextColor(ContextCompat.getColor(this, R.color.visited))

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
