package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.domain.medications.model.IntakeStatus
import java.time.Instant

@Entity(
    tableName = "intakes",
    indices = [Index(value = ["medicationId"])],
)
data class IntakeEntity(
    @PrimaryKey
    val id: String,
    val medicationId: String,
    val presetTime: String?,
    val timestamp: Instant,
    val status: IntakeStatus,
)
