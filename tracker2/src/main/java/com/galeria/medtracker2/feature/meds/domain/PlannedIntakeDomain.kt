package com.galeria.medtracker2.feature.meds.domain

import java.time.Instant
import java.util.UUID

data class PlannedIntakeDomain(
    val id: UUID,
    val medicationCourseId: UUID,
    val scheduledIntakeDateTime: Instant
)