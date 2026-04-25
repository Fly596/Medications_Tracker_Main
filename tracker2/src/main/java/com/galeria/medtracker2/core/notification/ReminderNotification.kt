package com.galeria.medtracker2.core.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.galeria.medtracker2.R

// Элемент уведомления.
class ReminderNotification(private val context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showNotification(title: String?) {
        val notification =
            NotificationCompat.Builder(context, "channel_id")
                .setContentText("Time to get paper")
                .setContentTitle("Hello Broke Nigga")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(1, notification)
    }

}