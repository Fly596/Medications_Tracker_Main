package com.galeria.medtracker2.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedsAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: MedsNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        android.util.Log.d("MedsAlarmReceiver", "БАТАРЕЯ ОГОНЬ, МЫ ПРОСНУЛИСЬ!")

        // 1. получаем переданные будильнику данные (как из письма).
        val medName = intent?.getStringExtra("MED_NAME") ?: "Unknown medication"
        val medId = intent?.getIntExtra("MED_ID", 0) ?: 0
        val medMessage = intent?.getStringExtra("MED_MESSAGE") ?: "Unknown message"

        // Показываем уведомление.
        notificationManager.showMedsNotification(
            title = "Sup nigga",
            message = "Time to take your $medName",
        )
    }
}
