package com.galeria.medicationstracker.feature_medications.data.source.remote

import com.galeria.medicationstracker.data.source.local.entities.Dosage
import kotlin.time.Instant

data class MedicationDto(
    val id: Int,
    val name: String,
    val dosage: Dosage,
    val startDate: Instant?,
    val endDate: Instant?,
    val daysOfWeek: List<String>,
    val intakeTime: Int,
)
