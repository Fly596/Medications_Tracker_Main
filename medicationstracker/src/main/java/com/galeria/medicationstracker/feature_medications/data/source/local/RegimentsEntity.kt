package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "regiments",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RegimentsEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(index = true)
    val medicationId: String,
    val startDate: Long,
    val endDate: Long?, // null - вечно.
    val frequencyType: String,
    val frequencyDetails: String?, // Если SPECIFIC_DAYS -> [MONDAY, WEDNESDAY].
    val timeSlots: List<String>,
    val dosage: Double,
)

