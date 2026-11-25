package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.data.MedicationForm
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val dosage: String,
    val form: MedicationForm,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val daysOfWeek: List<String>,
    val intakeTime: LocalTime,
)

