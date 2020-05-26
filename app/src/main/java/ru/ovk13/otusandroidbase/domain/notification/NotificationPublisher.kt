package ru.ovk13.otusandroidbase.domain.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.ovk13.otusandroidbase.presentation.MainActivity
import ru.ovk13.otusandroidbase.presentation.filmdetail.FilmDetailFragment

class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            intent.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)

        val filmId = intent.getIntExtra(FILM_ID, 0)
        if (filmId > 0) {
            val actionIntent = Intent(context, MainActivity::class.java)
            actionIntent.putExtra(FilmDetailFragment.ID, filmId.toString())
                .putExtra(MainActivity.OPEN_FILM_DETAIL, "1")
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            );
            notification?.contentIntent = pendingIntent
        }

        notificationManager.notify(id, notification)
    }

    companion object {
        const val FILM_ID = "film_id"
        const val NOTIFICATION_ID = "notification_id"
        const val NOTIFICATION = "notification"
    }
}