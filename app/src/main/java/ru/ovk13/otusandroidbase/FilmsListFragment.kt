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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_films_list.view.*
import ru.ovk13.otusandroidbase.data.Film
import ru.ovk13.otusandroidbase.ui.adapters.FilmViewAdapter
import ru.ovk13.otusandroidbase.ui.decorations.LineItemDecoration

class FilmsListFragment : Fragment() {

    private var listener: FilmListListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_films_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        recyclerView.adapter =
            FilmViewAdapter(
                LayoutInflater.from(context),
                if (listType == FilmViewAdapter.TYPE_FAVOURITES) getString(
                    R.string.favourites
                ) else "",
                listType,
                getListItems(listType),
                { listener?.onDetailsClick(it) },
                { filmItem, position ->
                    listener?.onToggleFavouritesClick(
                        filmItem,
                        recyclerView,
                        position
                    )
                },
                { filmItem, recyclerPosition ->
                    listener?.onRemoveFromFavouritesClick(
                        filmItem,
                        recyclerView,
                        recyclerPosition
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

        if (listType == FilmViewAdapter.TYPE_LIST) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    listener?.onListScroll(recyclerView)
                }
            })

            view.pullToRefresh.isEnabled = true
            view.pullToRefresh.setOnRefreshListener {
                listener?.onRefresh(view.pullToRefresh, recyclerView.adapter as FilmViewAdapter)
            }
        } else {
            view.pullToRefresh.isEnabled = false
        }

        initButtons(listType, view, recyclerView)
    }

    private fun getListItems(listType: Int): MutableList<Film?> {

        val fullFilmsList = (activity as MainActivity).filmsList ?: mutableListOf()

        if (listType == FilmViewAdapter.TYPE_LIST) {
            return fullFilmsList
        } else {
            val favouritesFilmsList = mutableListOf<Film?>()
            (activity as MainActivity).favouriteFilmsIds.forEach { id ->
                val filmItem = fullFilmsList.find { it?.id == id }
                if (filmItem !== null) {
                    favouritesFilmsList.add(filmItem)
                }
            }
            return favouritesFilmsList
        }
    }

    private fun initButtons(listType: Int, view: View, recyclerView: RecyclerView) {

        val addBtn = view.findViewById<ImageButton>(R.id.addBtn)
        val inviteBtn = view.findViewById<ImageButton>(R.id.inviteBtn)

        if (listType == FilmViewAdapter.TYPE_FAVOURITES) {
//            initAddButton(addBtn, recyclerView)
//            showButton(addBtn)
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
        if (activity is FilmListListener) {
            listener = activity as FilmListListener
        } else {
            throw Exception("No FilmClickListener")
        }

        initRecyclerView(view!!)
    }

    interface FilmListListener {
        fun onDetailsClick(filmItem: Film)
        fun onToggleFavouritesClick(
            filmItem: Film,
            recyclerView: RecyclerView,
            position: Int
        ) {
        }

        fun onRemoveFromFavouritesClick(
            filmItem: Film,
            recyclerView: RecyclerView,
            recyclerPosition: Int
        ) {
        }

        fun onAddToFavouritesClick(
            id: Int,
            recyclerView: RecyclerView
        ) {
        }

        fun onListScroll(
            recyclerView: RecyclerView
        ) {
        }

        fun onRefresh(
            pullToRefresh: SwipeRefreshLayout,
            adapter: FilmViewAdapter
        ) {
        }
    }

    companion object {
        const val TAG = "filmsListFragment"
        const val LIST_ITEMS = "filmsListItems"
        const val LIST_TYPE = "filmsListType"

        fun newInstance(type: Int): FilmsListFragment {
            val fragment = FilmsListFragment()

            val bundle = Bundle()
            bundle.putInt(LIST_TYPE, type)

            fragment.arguments = bundle

            return fragment
        }
    }
}