package com.galeria.medicationstracker.core.domain.model

import com.galeria.medicationstracker.core.database.entity.DayOfWeek
import java.time.Instant

data class Medication(
  val id: String,
  val userId: String,
  val name: String,
  val dosage: String,
  val form: String,
  val startDate: Instant,
  val endDate: Instant,
  val daysOfWeek: List<DayOfWeek>,
  val intakeTime: String
)
