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
    // Детали частоты (JSON или через TypeConverter).
    // Если SPECIFIC_DAYS -> [MONDAY, WEDNESDAY].
    // Если INTERVAL -> 2 (раз в 2 дня).
    val frequencyType: FrequencyType,
    val frequencyDetails: String?,
    val timeSlots: List<Instant>,
    val dosage: Double,
)

