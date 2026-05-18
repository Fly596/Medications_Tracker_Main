package com.galeria.medtracker2.domain.model

import java.time.Instant
import java.util.UUID

data class IntakeLogDomain(
    val id: UUID,
    val plannedIntakeId: UUID,
    val actualTimestamp: Instant,
    val isTaken: Boolean,
    val notes: String = "",
)