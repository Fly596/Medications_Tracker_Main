package com.galeria.medtracker2.core.common.data

data class MedicationCourseSummary(
    val name: String,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long,
)
