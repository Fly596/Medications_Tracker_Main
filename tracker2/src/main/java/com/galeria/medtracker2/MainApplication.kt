package com.galeria.medtracker2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.galeria.medtracker2.core.notifications.NotificationConstants.MED_CHANNEL_DESCRIPTION
import com.galeria.medtracker2.core.notifications.NotificationConstants.MED_CHANNEL_ID
import com.galeria.medtracker2.core.notifications.NotificationConstants.MED_CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Уровень важности.
            val importance = NotificationManager.IMPORTANCE_HIGH
            // Создаем канал уведомления.
            val channel = NotificationChannel(MED_CHANNEL_ID, MED_CHANNEL_NAME, importance)
                .apply {
                    description = MED_CHANNEL_DESCRIPTION
                }

            // Достаем сервис уведомлений.
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            // Регистрируем канал.
            notificationManager.createNotificationChannel(channel)
        }
    }
}
