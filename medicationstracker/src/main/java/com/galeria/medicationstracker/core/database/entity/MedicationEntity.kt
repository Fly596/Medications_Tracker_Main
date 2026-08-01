package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.data.MedicationForm
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
enum class DayOfWeek {

  MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

@Entity(tableName = "medications")
data class MedicationEntity(
  @PrimaryKey()
  val id: String,
  val userId: String,
  val name: String,
  val dosage: String,
  val form: String = MedicationForm.UNKNOWN.name,
  val startDate: Instant,
  val endDate: Instant,
  val intakeTime: String,
)

