package com.galeria.medtracker2.feature_meds.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,
    // В 1 единице (таблетке), для не мед препаратов мб упустить.
    val doseMg: Double?,
    val stock: Double?, // Кол-во штук/грамм..
    val stockMeasureUnit: String?, // Показывает это штуки или граммы.
    val drugClass: String?, // stim/opioid/benz..
)
