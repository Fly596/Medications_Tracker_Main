package com.galeria.medtracker2.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.galeria.medtracker2.R

object NotificationHelper {

    private const val CHANNEL_ID = "medication_channel"

    fun showNotification(context: Context, id: Int, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Medications reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for medications intakes reminders"
        }

        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // смахнется после клика.
            .build()

        notificationManager.notify(id, notification)

    }

}