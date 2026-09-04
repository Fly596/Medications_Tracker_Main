package com.galeria.medtracker2.domain.model

import java.time.Instant
import java.util.UUID

data class IntakeDomain(
    val id: Long,
    val medicationId: UUID,
    val dose: Dose,
    val cost: Money?,
    val intakeDateTime: Instant,
)
