package com.galeria.medtracker2.feature.meds.data.local.intakes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    val id: Uuid = Uuid.random(),
    val medicationId: Uuid,
    val doseMg: Double?,
    val startDate: Instant,
    val endDate: Instant?,
)