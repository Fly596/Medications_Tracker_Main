package com.galeria.medtracker2.feature.meds.data.local.combined

data class RegimentWithNameDoseDate(
    val name: String,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long
)