package com.galeria.medtracker2.core.notification.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.galeria.medtracker2.core.notification.ReminderReceiver
import com.galeria.medtracker2.core.notification.ScheduleNotificationRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class ScheduleNotificationRepoImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : ScheduleNotificationRepo {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(
        scheduleId: UUID, timeMillis: Long, title: String, dose: String
    ) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java).apply {
            putExtra("EXTRA_TITLE", title)
            putExtra("EXTRA_DOSE", dose)
        }

        /**
         * Важно: requestCode должен быть уникальным!
         * Мы используем hashCode от UUID записи расписания.
         * Это гарантирует, что каждый прием — это отдельный аларм в системе.
         */
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Для Android 12+ (API 31) нужно разрешение SCHEDULE_EXACT_ALARM

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent
            )
        } else {
            // Если нет разрешения, используем обычный сеттер
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMillis,
                pendingIntent
            )
        }

    }
}