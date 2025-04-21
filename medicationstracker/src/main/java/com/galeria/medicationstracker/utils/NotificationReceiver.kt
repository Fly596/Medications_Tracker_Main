package com.galeria.medicationstracker.utils

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.galeria.medicationstracker.R
import java.util.Random

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message") ?: "Пора жрать таблетки, мать твою!"
        sendNotification(context, message)
    }

    private fun sendNotification(context: Context, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "pill_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Иконку свою пихай, не забудь
            .setContentTitle("Пора жрать пилюли, лентяй!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(
                Random().nextInt(),
                notification
            ) // Random ID, чтоб не перекрывались
        } else {
            // Если прав нет, пиздец, проси их у юзера
            // Например, через ActivityResultLauncher в Compose
        }
    }

    fun scheduleNotification(context: Context, timeInMillis: Long, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Random().nextInt(), // Уникальный ID для каждого уведомления
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

}