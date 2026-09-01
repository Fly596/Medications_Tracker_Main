package com.galeria.medtracker2.domain.model

import java.time.Instant
import java.util.UUID

data class IntakeDomain(
    val id: Int = 0,
    val medicationId: UUID,
    val amount: Int,
    val unit: String,
    val cost: Double,
    val intakeDateTime: Instant,
)
