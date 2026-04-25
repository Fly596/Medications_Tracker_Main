package com.galeria.medtracker2.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { ReminderNotification(it) }
        val title: String? = "" // TODO: intent?.getStringExtra()
        scheduleNotificationService?.showNotification(title)
    }
}