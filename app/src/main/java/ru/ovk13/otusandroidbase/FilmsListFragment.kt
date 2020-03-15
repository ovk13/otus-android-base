package ru.ovk13.otusandroidbase

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ovk13.otusandroidbase.recycler.FilmItem
import ru.ovk13.otusandroidbase.recycler.FilmViewAdapter
import ru.ovk13.otusandroidbase.recycler.decorations.LineItemDecoration

class FilmsListFragment : Fragment() {

    private var listener: FilmClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_films_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {

        val listType = arguments?.getInt(LIST_TYPE) ?: FilmViewAdapter.TYPE_LIST
        val recyclerView = view.findViewById<RecyclerView>(R.id.filmsRecycler)
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            } else {
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FilmViewAdapter(
            LayoutInflater.from(context),
            if (listType == FilmViewAdapter.TYPE_FAVOURITES) getString(R.string.favourites) else "",
            listType,
            arguments?.getIntArray(LIST_ITEMS)?.toMutableList() ?: mutableListOf(),
            { listener?.onDetailsClick(it) },
            { filmItem, position ->
                listener?.onToggleFavouritesClick(
                    filmItem,
                    recyclerView,
                    position
                )
            },
            { filmItem, recyclerPosition, favouritesPosition ->
                listener?.onRemoveFromFavouritesClick(
                    filmItem,
                    recyclerView,
                    recyclerPosition,
                    favouritesPosition
                )
            }
        )
        val decorator = LineItemDecoration(
            10,
            30,
            ContextCompat.getColor(context!!, R.color.filmDecorator),
            7f
        )
        recyclerView.addItemDecoration(decorator)

        initButtons(listType, view, recyclerView)
    }

    private fun initButtons(listType: Int, view: View, recyclerView: RecyclerView) {

        val addBtn = view.findViewById<ImageButton>(R.id.addBtn)
        val inviteBtn = view.findViewById<ImageButton>(R.id.inviteBtn)

        if (listType == FilmViewAdapter.TYPE_FAVOURITES) {
            initAddButton(addBtn, recyclerView)
            showButton(addBtn)
            hideButton(inviteBtn)
        } else {
            initInviteButton(inviteBtn)
            showButton(inviteBtn)
            hideButton(addBtn)
        }

    }

    private fun showButton(button: ImageButton) {
        button.visibility = View.VISIBLE
    }

    private fun hideButton(button: ImageButton) {
        button.visibility = View.GONE
    }

    private fun initAddButton(button: ImageButton, recyclerView: RecyclerView) {
        // Заглушка. Просдо добавляет новый фильм в список избранного
        button.setOnClickListener {
            listener?.onAddToFavouritesClick(1, recyclerView)
        }
    }

    private fun initInviteButton(button: ImageButton) {
        button.setOnClickListener {
            val inviteIntent = Intent(Intent.ACTION_SEND)
            inviteIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inviteTitle))
            inviteIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.inviteText))
            inviteIntent.type = "text/plain"
            val inviteChooser =
                Intent.createChooser(inviteIntent, getString(R.string.inviteChooserTitle))
            inviteIntent.resolveActivity((activity as AppCompatActivity).packageManager)?.let {
                startActivity(inviteChooser)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.show()

        super.onActivityCreated(savedInstanceState)

        Log.d("LISTENER_INIT", activity.toString())
        if (activity is FilmClickListener) {
            listener = activity as FilmClickListener
        } else {
            throw Exception("No FilmClickListener")
        }
    }

    interface FilmClickListener {
        fun onDetailsClick(filmItem: FilmItem)
        fun onToggleFavouritesClick(
            filmItem: FilmItem,
            recyclerView: RecyclerView,
            position: Int
        ) {
        }

        fun onRemoveFromFavouritesClick(
            filmItem: FilmItem,
            recyclerView: RecyclerView,
            recyclerPosition: Int,
            favouritesPosition: Int
        ) {
        }

        fun onAddToFavouritesClick(
            id: Int,
            recyclerView: RecyclerView
        ) {
        }

    }

    companion object {
        const val TAG = "filmsListFragment"
        const val LIST_ITEMS = "filmsListItems"
        const val LIST_TYPE = "filmsListType"

        fun newInstance(filmsListIds: IntArray, type: Int): FilmsListFragment {
            val fragment = FilmsListFragment()

            val bundle = Bundle()
            bundle.putIntArray(LIST_ITEMS, filmsListIds)
            bundle.putInt(LIST_TYPE, type)

            fragment.arguments = bundle

            return fragment
        }
    }
}