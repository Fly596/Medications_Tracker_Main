package com.galeria.medtracker2.core.notifications.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.galeria.medtracker2.core.notifications.AlarmItem
import com.galeria.medtracker2.core.notifications.ReminderReceiver
import com.galeria.medtracker2.core.notifications.ScheduleNotificationRepo
import com.galeria.medtracker2.core.utils.DateTimeUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ScheduleNotificationRepoImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) :
    ScheduleNotificationRepo {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val formatedItemDateTime =
                DateTimeUtils.formatLocalDateTime(
                    DateTimeUtils.fromLongToLocalDateTime(item.timeMillis)
                )
        val intent =
                Intent(context, ReminderReceiver::class.java).apply {
                    putExtra("EXTRA_TITLE", item.title)
                    putExtra("EXTRA_DOSE", item.dose)
                    putExtra("EXTRA_TIME", formatedItemDateTime)
                }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.scheduleId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        /**
         * Важно: requestCode должен быть уникальным! Мы используем hashCode от UUID записи
         * расписания. Это гарантирует, что каждый прием — это отдельный аларм в системе.
         */
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.timeMillis,
            pendingIntent
        )
    }
}
