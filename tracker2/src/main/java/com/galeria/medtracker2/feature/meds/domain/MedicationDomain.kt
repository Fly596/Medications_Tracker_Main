package com.galeria.medtracker2.feature.meds.domain

import java.time.Instant
import java.util.UUID

data class MedicationDomain(
    val id: UUID, val name: String, val creationTimestamp: Instant
)
