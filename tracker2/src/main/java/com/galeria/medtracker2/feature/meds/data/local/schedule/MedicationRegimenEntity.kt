package com.galeria.medtracker2.feature.meds.data.local.schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import java.time.Instant
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "medications_regimens",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ]
)
data class MedicationRegimenEntity constructor(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val medicationId: UUID,
    val doseMg: Double,
    val startDate: Instant,
    val endDate: Instant?,
)


