package com.galeria.medtracker2.domain.model

import java.time.Instant
import java.util.UUID

data class MedicationDomain(
    val id: UUID,
    val name: String,
    val creationTimestamp: Instant
)
