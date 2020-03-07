package ru.ovk13.otusandroidbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ru.ovk13.otusandroidbase.recycler.FilmItem


class FilmsDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_films_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmItem: FilmItem = arguments?.getParcelable(FILM_ITEM)!!

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(filmItem.titleResId)
        view.findViewById<TextView>(R.id.filmDescription).setText(filmItem.descriptionResId)
        view.findViewById<ImageView>(R.id.filmCover).setImageResource(filmItem.coverResId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.hide()

        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        const val TAG = "FilmsDetailFragment"
        const val FILM_ITEM = "FilmItem"

        fun newInstance(filmItem: FilmItem): FilmsDetailsFragment {
            val fragment = FilmsDetailsFragment()

            val bundle = Bundle()
            bundle.putParcelable(FILM_ITEM, filmItem)

            fragment.arguments = bundle

            return fragment
        }
    }
}
