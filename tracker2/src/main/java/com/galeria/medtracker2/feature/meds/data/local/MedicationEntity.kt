package com.galeria.medtracker2.feature.meds.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    // В 1 единице (таблетке), для не мед препаратов мб упустить.
    val doseMg: Double?,
)
