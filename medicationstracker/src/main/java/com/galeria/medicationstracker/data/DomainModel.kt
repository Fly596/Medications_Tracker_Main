package com.galeria.medicationstracker.data

import java.time.LocalDate

data class DomainMedication(
    val id: Int,
    val name: String,
    val dosageValue: Double,
    val dosageUnit: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val daysOfWeek: List<String>,
    val intakeTime: Int,
)
