package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.feature_medications.domain.model.FrequencyType
import java.time.Instant
import java.util.UUID

// enum class FrequencyType {
//     EVERYDAY,
//     SPECIFIC_DAYS,
//     INTERVAL,
//     AS_NEEDED,
// }
@Entity(
    tableName = "medication-regiments",
    foreignKeys =
        [
            ForeignKey(
                entity = MedicationEntity::class,
                parentColumns = ["id"],
                childColumns = ["medicationId"],
            )
        ],
)
data class RegimentEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val medicationId: String,
    val startDate: Instant,
    val endDate: Instant?, // null - вечно.
    val frequencyType: FrequencyType,
    // Детали частоты (JSON или через TypeConverter).
    // Если SPECIFIC_DAYS -> [MONDAY, WEDNESDAY].
    // Если INTERVAL -> 2 (раз в 2 дня).
    val frequencyDetails: String?,
    // Время приемов в течение дня (JSON List<LocalTime>).
    // Например: ["08:00", "14:00", "20:00"].
    val timeSlots: List<Instant>,
    val dosage: Double,
)
