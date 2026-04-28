package com.galeria.medtracker2.feature.meds.domain

import java.util.UUID

data class IntakeDomain(
    val id: UUID,
    val medicationScheduleId: UUID,
    val actualIntakeDateTime: java.time.Instant,
    val notes: String = ""
)

