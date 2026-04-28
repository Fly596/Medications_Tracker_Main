package com.galeria.medtracker2.feature.intakes.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeEntity
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "intakes",
    foreignKeys = [
        ForeignKey(
            entity = ScheduledDateTimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationScheduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IntakeEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val medicationScheduleId: UUID,
    val actualIntakeDateTime: Long,
    val notes: String?
)

