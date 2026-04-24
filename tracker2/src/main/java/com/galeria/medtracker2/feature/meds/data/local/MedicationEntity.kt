@file:OptIn(ExperimentalUuidApi::class)

package com.galeria.medtracker2.feature.meds.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: Uuid = Uuid.random(),
    val name: String,
    val intakeTimeSeconds: Int,
)

@Entity(
    tableName = "medications_regimens",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationRegimenEntity(
    @PrimaryKey
    val id: Uuid = Uuid.random(),
    val medicationId: Uuid,
    val doseMg: Double?,
    val startDate: Instant,
    val endDate: Instant?,
)

@Entity(
    tableName = "scheduled_date_times",
    foreignKeys = [
        ForeignKey(
            entity = MedicationRegimenEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationScheduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScheduledDateTimeEntity(
    @PrimaryKey
    val id: Uuid = Uuid.random(),
    val medicationScheduleId: Uuid,
    val scheduledIntakeDateTime: Instant,
)

@Entity(tableName = "intakes")
data class IntakeEntity(
    val id: Uuid = Uuid.random(),

    )