package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.feature_medications.domain.model.FrequencyType
import java.time.Instant
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
    val startDate: Instant,
    val endDate: Instant?, // null - вечно.
    val frequencyType: FrequencyType,
    // Детали частоты (JSON или через TypeConverter).
    // Если frequencyType = SPECIFIC_DAYS -> [MONDAY, WEDNESDAY].
    // Если frequencyType = INTERVAL -> 2 (раз в 2 дня).
    val frequencyDetails: String?,
    val timeSlots: List<Int>,
    val dosage: Double,
)

