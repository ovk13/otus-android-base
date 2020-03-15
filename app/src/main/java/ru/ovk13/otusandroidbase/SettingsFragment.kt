package ru.ovk13.otusandroidbase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDayNightSwitcher(view)
    }

    private fun initDayNightSwitcher(view: View) {
        val dayNightSwitcher = view.findViewById<Switch>(R.id.dayNightSwitcher)
        dayNightSwitcher.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        Log.d("EVENT", dayNightSwitcher.isChecked.toString())
        dayNightSwitcher?.setOnCheckedChangeListener { _, isChecked ->
            Log.d("EVENT", isChecked.toString())
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.show()

        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        const val TAG = "settingsFragment"
    }
}