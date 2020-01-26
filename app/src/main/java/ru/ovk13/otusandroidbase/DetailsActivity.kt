package ru.ovk13.otusandroidbase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.ovk13.otusandroidbase.MainActivity.Companion.COMMENT
import ru.ovk13.otusandroidbase.MainActivity.Companion.FILM_DATA
import ru.ovk13.otusandroidbase.MainActivity.Companion.LIKE

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val filmData: FilmData = getIntent().getParcelableExtra<FilmData>(FILM_DATA)
        findViewById<TextView>(R.id.filmName).setText(filmData.nameResId)
        findViewById<TextView>(R.id.filmDescription).setText(filmData.descriptionResId)
        findViewById<ImageView>(R.id.filmCover).setImageResource(filmData.coverResId)


    }

    override fun onBackPressed() {

        val like = findViewById<CheckBox>(R.id.likeCb)
        val comment = findViewById<EditText>(R.id.comment)

        val intent = Intent()
        intent.putExtra(LIKE, like.isChecked)
        intent.putExtra(COMMENT, comment.text.toString())
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }
}
