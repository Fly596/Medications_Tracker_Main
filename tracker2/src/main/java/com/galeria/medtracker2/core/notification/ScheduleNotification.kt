package com.galeria.medtracker2.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

interface ScheduleNotificationRepo {

    fun schedule(
        scheduleId: UUID, timeMillis: Long, title: String, dose: String
    )
}

class ScheduleNotification @Inject constructor(
    @ApplicationContext
    private val context: Context
) : ScheduleNotificationRepo {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(
        scheduleId: UUID, timeMillis: Long, title: String, dose: String
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
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
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
        }

    }
//    override fun scheduleNotification(
//        context: Context,
//        timePickerState: TimePickerState,
//        datePickerState: DatePickerState,
//        title: String,
//    ) {
//        // Сообщение о намерении выполнить действие.
//        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
//        // "упакованный" intent, который передается другому сервису/приложению, чтобы тот выполнил
//        // его позже от нашего имени.
//        val pendingIntent = PendingIntent.getBroadcast(
//            context.applicationContext,
//            1,
//            intent,
//            PendingIntent.FLAG_MUTABLE
//        )
//
//        // Нужен для запуска действий приложения в будущем в заданные моменты времени.
//        val selectedDate = java.util.Calendar.getInstance().apply {
//            timeInMillis = datePickerState.selectedDateMillis!!
//        }
//
//        val year = selectedDate.get(Calendar.YEAR)
//        val month = selectedDate.get(Calendar.MONTH)
//        val day = selectedDate.get(Calendar.DAY_OF_MONTH)
//
//        val calendar = Calendar.getInstance()
//        //calendar.set(year, month, day, selectedHour,selectedMinute)
//        calendar.set(year, month, day, timePickerState.hour, timePickerState.minute)
//
//        // Устанавливает оповещение в определенное время, даже если приложение закрыто.
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//
//    }
}