package com.galeria.medtracker2.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "intakes",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("medicationId")]
)
data class IntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val medicationId: UUID,
    val amount: Int,
    val unit: String,
    val cost: Double,
    val intakeDateTime: Instant,
)
