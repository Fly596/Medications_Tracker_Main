package com.galeria.medtracker2.feature_meds.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val stock: Double?,
    val measureUnit: String?,
)
