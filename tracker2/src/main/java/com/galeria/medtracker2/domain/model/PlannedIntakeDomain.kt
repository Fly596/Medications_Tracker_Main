package com.galeria.medtracker2.domain.model

import java.time.Instant
import java.util.UUID

data class PlannedIntakeDomain(
    val id: UUID,
    val courseId: UUID,
    val scheduledTimestamp: Instant,
)
