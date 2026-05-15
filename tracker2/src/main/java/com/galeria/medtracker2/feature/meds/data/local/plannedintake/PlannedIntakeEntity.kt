package com.galeria.medtracker2.feature.meds.data.local.plannedintake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.data.local.course.MedicationCourseEntity
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "planned_intakes", // Было scheduled_date_times
    foreignKeys =
        [
            ForeignKey(
                entity = MedicationCourseEntity::class,
                parentColumns = ["id"],
                childColumns = ["courseId"],
                onDelete = ForeignKey.Companion.CASCADE,
            )
        ],
    indices = [Index("courseId")],
)
data class PlannedIntakeEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val courseId: UUID, // medicationScheduleId
    val scheduledTimestamp: Long, // Было scheduledIntakeDateTime
)
