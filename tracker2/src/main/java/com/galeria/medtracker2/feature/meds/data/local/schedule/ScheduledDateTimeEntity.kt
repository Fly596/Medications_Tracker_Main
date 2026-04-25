package com.galeria.medtracker2.feature.meds.data.local.schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "scheduled_date_times",
    foreignKeys = [
        ForeignKey(
            entity = MedicationRegimenEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationScheduleId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ]
)
data class ScheduledDateTimeEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val medicationScheduleId: UUID,
    val scheduledIntakeDateTime: Instant,
)
