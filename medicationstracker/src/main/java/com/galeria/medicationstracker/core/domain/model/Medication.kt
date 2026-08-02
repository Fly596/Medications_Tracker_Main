package com.galeria.medicationstracker.core.domain.model

import com.galeria.medicationstracker.data.MedicationForm
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class Medication(
  val id: String,
  val userId: String,
  val name: String,
  val dosage: String,
  val form: MedicationForm,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val daysOfWeek: List<DayOfWeek>,
  val intakeTime: LocalTime
)
