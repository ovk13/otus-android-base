package ru.ovk13.otusandroidbase.presentation.scheduleEditor

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_schedule_editor.*
import ru.ovk13.otusandroidbase.FilmsApplication
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import java.util.*

class ScheduleEditorFragment : Fragment() {

    private var editorIsOpened = false
    private var scheduleEditorViewModel: ScheduleEditorViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleTimePicker.setIs24HourView(true)

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
        }
        scheduleEditorViewModel!!.filmSchedule.observe(
            this.viewLifecycleOwner,
            Observer { schedule ->
                if (schedule != null) {
                    setDateTime(schedule)
                }
            })
        scheduleEditorViewModel!!.film.observe(
            this.viewLifecycleOwner,
            object : Observer<FilmDataModel> {
                override fun onChanged(film: FilmDataModel) {
                    initButtons()
                    scheduleFilmTitle.text = film.title
                    scheduleEditorViewModel!!.film.removeObserver(this)
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
        if (Build.VERSION.SDK_INT < 23) {
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

            findNavController().popBackStack()
        }
        submitEditSchedule.setOnClickListener {

            try {
                val hour: Int
                val minute: Int
                if (Build.VERSION.SDK_INT < 23) {
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
            } catch (e: Throwable) {

            }
        }
    }

    companion object {
        const val ID = "id"
    }
}