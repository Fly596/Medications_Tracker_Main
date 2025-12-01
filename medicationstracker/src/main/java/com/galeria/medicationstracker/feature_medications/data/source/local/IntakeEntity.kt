package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.data.source.network.IntakeStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.util.UUID
import kotlin.time.Instant

@Entity(tableName = "intakes")
data class IntakeEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val scheduledForDate: LocalDate,
    val scheduledForTime: LocalTime?,
    val takenAt: Instant,
    val status: IntakeStatus,
    val actualDosage: Double,
)
