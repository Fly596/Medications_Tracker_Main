package com.galeria.medtracker2.core.notifications

/*
class ReminderReceiver : BroadcastReceiver() {

    // Вызывается системой когда приходит время.
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context==null || intent==null) return

        // 1. получаем переданные данные (как из письма).
        val id = intent.getIntExtra("EXTRA_ID", 0)
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Reminder"
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "Time to take your meds"

        Log.d("ReminderReceiver", "Сработал будильник: $title, id: $id")

        // 2. Показываем уведы.
        NotificationHelper.showNotification(context, id, title, message)
    }
}*/
