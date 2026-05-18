package com.galeria.medtracker2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // В настройках тф каналы отображаются как категории уведов.
        // Создаем канал уведомления.
        val channel =
            NotificationChannel(
                "channel_id",
                "channel_name",
                NotificationManager.IMPORTANCE_HIGH,
            )
        channel.description = "description"

        val channelMain =
            NotificationChannel(
                "medication_reminders",
                "channel_reminders",
                NotificationManager.IMPORTANCE_HIGH,
            )
        channelMain.description = "medications_channel"

        // Регистрируем канал системой.
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(channelMain)
    }
}
