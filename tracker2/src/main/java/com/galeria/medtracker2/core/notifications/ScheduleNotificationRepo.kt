package com.galeria.medtracker2.core.notifications

interface ScheduleNotificationRepo {

    fun schedule(
        item: AlarmItem
    )
}
