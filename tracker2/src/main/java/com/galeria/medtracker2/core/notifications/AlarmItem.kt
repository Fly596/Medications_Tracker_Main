package com.galeria.medtracker2.core.notifications

import java.util.UUID

data class AlarmItem(
    val scheduleId: UUID,
    val timeMillis: Long,
    val title: String,
    val dose: String
)