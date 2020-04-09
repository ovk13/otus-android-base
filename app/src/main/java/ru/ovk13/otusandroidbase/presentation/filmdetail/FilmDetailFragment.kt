package ru.ovk13.otusandroidbase.presentation.filmdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel


class FilmDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_film_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmItem: FilmDataModel = arguments?.getParcelable(FILM_ITEM)!!

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = filmItem.title
        view.findViewById<TextView>(R.id.filmDescription).text = filmItem.overview

        val coverView = view.findViewById<ImageView>(R.id.filmCover)
        Glide.with(coverView.context)
            .load(filmItem.posterPath)
            .placeholder(R.drawable.ic_no_photo)
            .into(coverView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.hide()

        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        const val TAG = "FilmsDetailFragment"
        const val FILM_ITEM = "FilmItem"

        fun newInstance(filmItem: FilmDataModel): FilmDetailFragment {
            val fragment =
                FilmDetailFragment()

            val bundle = Bundle()
            bundle.putParcelable(FILM_ITEM, filmItem)

            fragment.arguments = bundle

            return fragment
        }
    }
}
