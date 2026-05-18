package com.galeria.medtracker2.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
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
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val medicationId: UUID,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long?,
)
