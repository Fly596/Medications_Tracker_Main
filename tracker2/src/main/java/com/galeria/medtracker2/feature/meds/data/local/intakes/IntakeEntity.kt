package com.galeria.medtracker2.feature.meds.data.local.intakes

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "intakes",
    foreignKeys = [
        ForeignKey(
            entity = ScheduledDateTimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationScheduleId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ]
)
data class IntakeEntity(
    val id: Uuid = Uuid.Companion.random(),
    val medicationScheduleId: Uuid,
    val actualIntakeDateTime: Instant,
    val notes: String?
)