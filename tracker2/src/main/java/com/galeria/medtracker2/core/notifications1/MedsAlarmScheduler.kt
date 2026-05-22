package com.galeria.medtracker2.core.notifications1

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.galeria.medtracker2.domain.model.AlarmItem
import com.galeria.medtracker2.domain.repository.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MedsAlarmScheduler @Inject constructor(
    @ApplicationContext
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(item: AlarmItem) {

        // --- ВОТ ЭТОТ КУСОК ДЛЯ ANDROID 12+ (API 31+) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Проверяем, разрешила ли нам система использовать точные будильники
            if (!alarmManager.canScheduleExactAlarms()) {
                // Если разрешения нет — кидаем юзера в системные настройки
                Toast.makeText(
                    context,
                    "Разрешите приложению слать точные уведомления!",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    // Передаем пакет нашего приложения, чтобы открылись настройки именно для нас
                    data = android.net.Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Нужен, так как запускаем не из Activity
                }
                context.startActivity(intent)
                return // Выходим, так как завести будильник мы сейчас физически не можем!
            }
        }

        // 1. Создаем интент для ReminderReceiver. (типа письмо).
        val intent = Intent(context, MedsAlarmReceiver::class.java)
            .apply {
                putExtra("MED_ID", item.id.hashCode())
                putExtra("MED_NAME", item.title)
                putExtra("MED_MESSAGE", item.message)
            }

        // Оборачиваем интент в pending intent.
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        // 3. Передаем системе.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.timeMillis,
                pendingIntent,
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, item.timeMillis, pendingIntent)
        }
    }

    override fun scheduleAll(items: List<AlarmItem>) {
        items.forEach { schedule(it) }
    }

    override fun cancel(item: AlarmItem) {
        // Чтобы отменить будильник, нужно воссоздать точно такой же PendingIntent и вызвать
        // cancel()
        val intent = Intent(context, MedsAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
            )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }
}
