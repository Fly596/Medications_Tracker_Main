package com.galeria.medtracker2.feature.meds.data.local.intakes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    val medicationScheduleId: Uuid,
    val scheduledIntakeDateTime: Instant,
)
