package com.galeria.medtracker2.core.common.data

data class RegimentWithNameDoseDate(
    val name: String,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long
)