package com.galeria.medtracker2.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "dosage_presets",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("medicationId")
    ]
)
data class DosagePresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val medicationId: UUID,
    val amount: Double,
    val unit: String,
    val name: String?
)