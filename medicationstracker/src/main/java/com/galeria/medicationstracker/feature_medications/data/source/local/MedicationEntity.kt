package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.feature_medications.domain.model.MedicationForm
import java.util.UUID

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val form: MedicationForm,
    val stockCount: Double?,
    val measureUnit: String,
    val drugClass: String,
)
