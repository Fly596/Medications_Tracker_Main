package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.galeria.medicationstracker.domain.medications.model.IntakeStatus
import java.time.Instant

@Entity(
    tableName = "intakes",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["medicationId"])],
)
data class IntakeEntity(
    @PrimaryKey
    val id: String,
    val medicationId: String,
    val presetTime: String? = null,
    val intakeTime: Instant = Instant.now(),
    val status: IntakeStatus = IntakeStatus.PENDING,
)
