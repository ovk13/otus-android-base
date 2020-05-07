package ru.ovk13.otusandroidbase.domain.notification

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.ovk13.otusandroidbase.R

class NotificationScheduler(
    private val base: Context,
    private val alarmManager: AlarmManager
) : ContextWrapper(base) {

    fun scheduleNotification(notification: Notification, publishDateTime: Long) {
        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(AlarmManager.RTC_WAKEUP, publishDateTime, pendingIntent)
    }

    fun getNotification(title: String, text: String): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setContentTitle(title)
        builder.setContentText(text)
        builder.setSmallIcon(R.drawable.ic_movie)
        return builder.build()
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Movie main"
            val description = "Movie application main channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "main_channel"
    }
}