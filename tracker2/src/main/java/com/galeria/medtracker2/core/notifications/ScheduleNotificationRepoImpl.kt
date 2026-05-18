package com.galeria.medtracker2.core.notifications

import java.util.UUID

interface ScheduleNotificationRepo {

    fun schedule(
        scheduleId: UUID, timeMillis: Long, title: String, dose: String
    )
}
