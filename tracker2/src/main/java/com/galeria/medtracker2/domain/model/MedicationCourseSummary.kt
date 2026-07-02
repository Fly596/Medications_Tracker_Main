package com.galeria.medtracker2.domain.model

import java.util.UUID

// Основная информация о приеме.
data class MedicationCourseSummary(
    val medicationId: UUID,
    val name: String,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long,
)
