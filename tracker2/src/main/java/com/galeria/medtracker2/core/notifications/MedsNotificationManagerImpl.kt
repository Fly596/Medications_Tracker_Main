package com.galeria.medtracker2.core.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.galeria.medtracker2.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface MedsNotificationManager {

    fun showMedsNotification(
        title: String,
        message: String,
    )
}

class MedsNotificationManagerImpl
@Inject
constructor(
    @ApplicationContext
    private val context: Context,
) : MedsNotificationManager {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showMedsNotification(
        title: String,
        message: String,
    ) {
        // Строим уведомление.
        val notification =
            NotificationCompat.Builder(context, NotificationConstants.MED_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // смахнется после клика.
                .build()

        // id должен быть уникальным для каждого уведомления.
        val notificationId = System.currentTimeMillis().toInt()

        // Показываем уведомление.
        notificationManager.notify(notificationId, notification)
    }
}
