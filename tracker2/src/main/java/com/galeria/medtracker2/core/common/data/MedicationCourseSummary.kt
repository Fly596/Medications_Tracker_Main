package com.galeria.medtracker2.core.common.data

import java.util.UUID

data class MedicationCourseSummary(
    val medicationId: UUID,
    val name: String,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long,
)
