package com.galeria.medtracker2.feature.meds.data.local.schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "scheduled_date_times",
    foreignKeys = [
        ForeignKey(
            entity = MedicationCourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationScheduleId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("medicationScheduleId")]
)
data class PlannedIntakeEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val medicationScheduleId: UUID,
    val scheduledIntakeDateTime: Long,
)
