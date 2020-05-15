package ru.ovk13.otusandroidbase.presentation.scheduleEditor

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_schedule_editor.*
import ru.ovk13.otusandroidbase.FilmsApplication
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import ru.ovk13.otusandroidbase.presentation.favouritefilmslist.FavouriteFilmsListViewModel
import ru.ovk13.otusandroidbase.presentation.favouritefilmslist.FavouriteFilmsListViewModelFactory
import ru.ovk13.otusandroidbase.presentation.filmslist.FilmsListViewModel
import ru.ovk13.otusandroidbase.presentation.filmslist.FilmsListViewModelFactory
import java.util.*

class ScheduleEditorFragment : Fragment() {

    private var editorIsOpened = false
    private var scheduleEditorViewModel: ScheduleEditorViewModel? = null
    private var filmsViewModel: FilmsListViewModel? = null
    private var favouritesViewModel: FavouriteFilmsListViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleDatePicker.minDate = System.currentTimeMillis()
        scheduleTimePicker.setIs24HourView(true)

        filmsViewModel = ViewModelProvider(
            activity!!,
            FilmsListViewModelFactory(
                FilmsApplication.instance!!.filmsUseCase,
                FilmsApplication.instance!!.favouritesUseCase,
                FilmsApplication.instance!!.visitedUseCase,
                FilmsApplication.instance!!.scheduleUseCase
            )
        ).get(
            FilmsListViewModel::class.java
        )

        favouritesViewModel =
            ViewModelProvider(
                activity!!,
                FavouriteFilmsListViewModelFactory(
                    FilmsApplication.instance!!.favouritesUseCase,
                    FilmsApplication.instance!!.visitedUseCase,
                    FilmsApplication.instance!!.scheduleUseCase
                )
            ).get(
                FavouriteFilmsListViewModel::class.java
            )

        scheduleEditorViewModel = ViewModelProvider(
            activity!!,
            ScheduleEditorViewModelFactory(
                FilmsApplication.instance!!.filmsUseCase,
                FilmsApplication.instance!!.scheduleUseCase
            )
        ).get(
            ScheduleEditorViewModel::class.java
        )


        scheduleEditorViewModel!!.setScheduleEditorOpened()
        scheduleEditorViewModel!!.scheduleEditorOpened.observe(
            this.viewLifecycleOwner,
            Observer { scheduleEditorOpened ->
                if (editorIsOpened && !scheduleEditorOpened) {
                    editorIsOpened = false
                    findNavController().popBackStack()
                } else if (!editorIsOpened && scheduleEditorOpened) {
                    editorIsOpened = true
                }
            })

        scheduleEditorViewModel!!.error.observe(this.viewLifecycleOwner, Observer { error ->
            if (error.isNotEmpty()) {
                showError(error)
                scheduleEditorViewModel!!.clearError()
            }
        })
        val filmId = arguments?.getInt(ID)
        if (filmId != null) {
            scheduleEditorViewModel!!.getFilmData(filmId)
            scheduleEditorViewModel!!.getFilmSchedule(filmId)
            initButtons()
        }
        scheduleEditorViewModel!!.filmSchedule.observe(
            this.viewLifecycleOwner,
            Observer { schedule ->
                if (schedule != null) {
                    setDateTime(schedule)
                }
            })
        scheduleEditorViewModel!!.film.observe(this.viewLifecycleOwner, Observer { film ->
            scheduleFilmTitle.text = film.title
        })
        scheduleEditorViewModel!!.setActionInProcess.observe(this.viewLifecycleOwner, Observer {
            if (!it) {
                val film = scheduleEditorViewModel!!.film.value
                if (film != null) {
                    filmsViewModel!!.setScheduledStatus(film.id, true)
                    favouritesViewModel!!.setScheduledStatus(film.id, true)
                }
            }
        })
        scheduleEditorViewModel!!.removeActionInProcess.observe(this.viewLifecycleOwner, Observer {
            if (!it) {
                val film = scheduleEditorViewModel!!.film.value
                if (film != null) {
                    filmsViewModel!!.setScheduledStatus(film.id, false)
                    favouritesViewModel!!.setScheduledStatus(film.id, false)
                }
            }
        })
    }

    private fun setDateTime(schedule: FilmScheduleModel) {
        val watchDate = Calendar.getInstance()
        watchDate.timeInMillis = schedule.watchTimestamp
        scheduleDatePicker.updateDate(
            watchDate.get(Calendar.YEAR),
            watchDate.get(Calendar.MONTH),
            watchDate.get(Calendar.DAY_OF_MONTH)
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            scheduleTimePicker.currentHour = watchDate.get(Calendar.HOUR_OF_DAY)
            scheduleTimePicker.currentMinute = watchDate.get(Calendar.MINUTE)
        } else {
            scheduleTimePicker.hour = watchDate.get(Calendar.HOUR_OF_DAY)
            scheduleTimePicker.minute = watchDate.get(Calendar.MINUTE)
        }
    }

    fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun initButtons() {
        cancelEditSchedule.setOnClickListener {
            scheduleEditorViewModel!!.setScheduleEditorClosed()
        }

        removeFromSchedule.setOnClickListener {
            scheduleEditorViewModel!!.startRemoveAction()
            scheduleEditorViewModel!!.removeFilmSchedule()
        }

        submitEditSchedule.setOnClickListener {
            scheduleEditorViewModel!!.startSetAction()
            val hour: Int
            val minute: Int
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                hour = scheduleTimePicker.currentHour
                minute = scheduleTimePicker.currentMinute
            } else {
                hour = scheduleTimePicker.hour
                minute = scheduleTimePicker.minute
            }

            scheduleEditorViewModel!!.setFilmSchedule(
                scheduleDatePicker.year,
                scheduleDatePicker.month,
                scheduleDatePicker.dayOfMonth,
                hour,
                minute
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar).title =
            resources.getString(R.string.scheduleFormTitle)

        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        const val ID = "id"
    }
}