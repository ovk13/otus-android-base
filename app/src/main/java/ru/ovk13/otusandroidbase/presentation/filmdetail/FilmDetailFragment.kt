package ru.ovk13.otusandroidbase.presentation.filmdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_film_detail.*
import ru.ovk13.otusandroidbase.FilmsApplication
import ru.ovk13.otusandroidbase.R


class FilmDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_film_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmDetailViewModel = ViewModelProvider(
            this,
            FilmDetailViewModelFactory(
                FilmsApplication.instance!!.filmsUseCase
            )
        ).get(FilmDetailViewModel::class.java)

        filmDetailViewModel.detailFilm.observe(this.viewLifecycleOwner, Observer { film ->
            detailToolbar.title = film.title
            view.findViewById<TextView>(R.id.filmDescription).text = film.overview

            val coverView = view.findViewById<ImageView>(R.id.filmCover)
            Glide.with(coverView.context)
                .load(film.getAbsolutePosterPath())
                .placeholder(R.drawable.ic_no_photo)
                .into(coverView)
        })

        val id = arguments?.getInt(ID, 0)
        if (id != null) {
            filmDetailViewModel.getFilm(id)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.hide()

        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        const val ID = "id"
        const val FILM_ITEM = "FilmItem"
    }
}
