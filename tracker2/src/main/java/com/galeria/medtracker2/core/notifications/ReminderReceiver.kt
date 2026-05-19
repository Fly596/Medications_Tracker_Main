package com.galeria.medtracker2.core.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.galeria.medtracker2.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            val title = intent?.getStringExtra("EXTRA_TITLE") ?: "Medicine"
            val dose = intent?.getStringExtra("EXTRA_DOSE") ?: ""
            val time = intent?.getStringExtra("EXTRA_TIME") ?: ""
            Log.i("ReminderReceiver", "title: $title, dose: $dose, time: $time")
            // var2
            showNotification(context, title, dose, time)
        }
    }

    private fun showNotification(context: Context, title: String, dose: String, time: String) {
        val channelId = "medication_reminders"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Time to take: $title")
                .setContentText("Time: $time\nDosage: $dose")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val scheduleNotificationService = context?.let { ReminderNotification(it) }
//        val title: String? = "" // TODO: intent?.getStringExtra()
//        scheduleNotificationService?.showNotification(title)
//    }
}