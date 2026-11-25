package com.galeria.medicationstracker.domain.medications.model

import com.galeria.medicationstracker.data.MedicationForm
import java.time.LocalDate
import java.time.LocalTime

data class Medication(
    val id: String,
    val name: String,
    val dosage: String,
    val form: MedicationForm,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val daysOfWeek: List<String>,
    val intakeTime: LocalTime
)
