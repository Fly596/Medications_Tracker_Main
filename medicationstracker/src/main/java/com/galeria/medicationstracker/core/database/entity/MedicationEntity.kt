package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "medications")
data class MedicationEntity(
  @PrimaryKey()
  val id: String,
  val name: String,
  val dosage: String,
  val form: String,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val daysOfWeek: List<DayOfWeek>,
  val intakeTime: LocalTime, // Колво секунд с начала дня.
)

