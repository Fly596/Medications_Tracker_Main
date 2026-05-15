package com.galeria.medtracker2.core.common.data

import java.util.UUID

data class ScheduledIntakeDetails(
    val plannedIntakeId: UUID,
    val courseId: UUID,
    val medicationName: String,
    val doseMg: Double,
    val scheduledTimestamp: Long,
    val isTaken: Boolean?,
)
