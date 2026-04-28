package com.galeria.medtracker2.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.TimePickerState
import javax.inject.Inject

interface ScheduleNotificationRepo {
    fun scheduleNotification(
        context: Context,
        timePickerState: TimePickerState,
        datePickerState: DatePickerState,
        title: String,
    )
}

class ScheduleNotification @Inject constructor() : ScheduleNotificationRepo {

    override fun scheduleNotification(
        context: Context,
        timePickerState: TimePickerState,
        datePickerState: DatePickerState,
        title: String,
    ) {
        // Сообщение о намерении выполнить действие.
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
        // "упакованный" intent, который передается другому сервису/приложению, чтобы тот выполнил
        // его позже от нашего имени.
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            1,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        // Нужен для запуска действий приложения в будущем в заданные моменты времени.
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val selectedDate = java.util.Calendar.getInstance().apply {
            timeInMillis = datePickerState.selectedDateMillis!!
        }

        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)

        val calendar = Calendar.getInstance()
        //calendar.set(year, month, day, selectedHour,selectedMinute)
        calendar.set(year, month, day, timePickerState.hour, timePickerState.minute)

        // Устанавливает оповещение в определенное время, даже если приложение закрыто.
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }
}