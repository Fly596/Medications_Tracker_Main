package com.galeria.medtracker2.feature.meds.domain

import java.time.Instant
import java.util.UUID

data class ScheduledDomain(
    val id: UUID,
    val medicationScheduleId: UUID,
    val scheduledIntakeDateTime: Instant
)