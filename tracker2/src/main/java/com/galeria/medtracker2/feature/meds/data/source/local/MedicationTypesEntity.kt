package com.galeria.medtracker2.feature.meds.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_types")
data class MedicationTypesEntity(
    @PrimaryKey
    val id: String,
    val name: String,
)
