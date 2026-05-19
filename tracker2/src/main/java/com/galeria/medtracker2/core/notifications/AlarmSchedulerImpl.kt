package com.galeria.medtracker2.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.galeria.medtracker2.domain.model.AlarmItem
import com.galeria.medtracker2.domain.repository.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(item: AlarmItem) {
        // 1. Создаем интент для ReminderReceiver. (типа письмо).
        val intent =
            Intent(context, ReminderReceiver::class.java).apply {
                putExtra("EXTRA_ID", item.id.hashCode())
                putExtra("EXTRA_TITLE", item.title)
                putExtra("EXTRA_MESSAGE", item.message)
            }

        // 2. Создаем pending intent (типа конверт).
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // 3. Передаем системе.
        if (alarmManager.canScheduleExactAlarms()) {
            // если есть разрешение на точные будильники.
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.timeMillis,
                pendingIntent
            )
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.timeMillis,
                pendingIntent
            )
        }
    }

    override fun scheduleAll(items: List<AlarmItem>) {
        items.forEach { schedule(it) }
    }

}