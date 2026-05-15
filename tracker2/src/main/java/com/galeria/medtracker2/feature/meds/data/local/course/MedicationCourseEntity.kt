package com.galeria.medtracker2.feature.meds.data.local.course

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "medication_courses", // Было medications_regimens
    foreignKeys =
        [
            ForeignKey(
                entity = MedicationEntity::class,
                parentColumns = ["id"],
                childColumns = ["medicationId"],
                onDelete = ForeignKey.CASCADE,
            )
        ],
    indices = [Index("medicationId")],
)
data class MedicationCourseEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val medicationId: UUID,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long?,
)
