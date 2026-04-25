package com.galeria.medtracker2.feature.meds.domain

import java.time.Instant
import java.util.UUID

data class MedicationRegimentDomain(
    val id: UUID,
    val medicationId: UUID,
    val doseMg: Double = 0.0,
    val startDate: Instant,
    val endDate: Instant = Instant.MAX
)
