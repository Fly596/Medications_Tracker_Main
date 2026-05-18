package com.galeria.medtracker2.domain.model

import java.time.Instant
import java.util.UUID

data class MedicationCourseDomain(
    val id: UUID,
    val medicationId: UUID,
    val doseMg: Double = 0.0,
    val startDate: Instant,
    val endDate: Instant = Instant.MAX
)
