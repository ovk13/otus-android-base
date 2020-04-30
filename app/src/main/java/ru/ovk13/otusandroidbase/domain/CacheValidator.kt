package ru.ovk13.otusandroidbase.domain

import android.content.SharedPreferences
import java.util.*

class CacheValidator(
    private val sharedPreferences: SharedPreferences
) {

    fun isValid(): Boolean {

        val curTime = Date()
        val lastLoadTime = getLastLoadTime()

        return if (lastLoadTime == null) {
            false
        } else {
            ((curTime.time - lastLoadTime.time) / 1000 < CACHE_TTL)
        }
    }

    fun isNotValid(): Boolean {
        return !isValid()
    }

    private fun getLastLoadTime(): Date? {
        try {
            val lastLoadTimestamp = sharedPreferences.getLong(SHARED_PREFERENCES_LAST_LOAD_TIME, 0)
            if (lastLoadTimestamp == 0L) {
                return null
            }
            return Date(lastLoadTimestamp)
        } catch (e: Throwable) {
            return null
        }
    }

    fun setLastLoadTime(time: Date) {
        val editor = sharedPreferences.edit()
        editor.putLong(SHARED_PREFERENCES_LAST_LOAD_TIME, time.time)
        editor.apply()
    }

    companion object {
        const val SHARED_PREFERENCES_LAST_LOAD_TIME = "LAST_LOAD_TIME"
        const val CACHE_TTL = 1200
    }
}