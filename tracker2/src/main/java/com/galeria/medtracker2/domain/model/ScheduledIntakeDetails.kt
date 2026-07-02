package com.galeria.medtracker2.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.galeria.medtracker2.core.database.entity.IntakeLogEntity
import com.galeria.medtracker2.core.database.entity.MedicationCourseEntity
import com.galeria.medtracker2.core.database.entity.MedicationEntity
import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity
import java.util.UUID

data class ScheduledIntakeDetails(
    val plannedIntakeId: UUID,
    val courseId: UUID,
    val medicationName: String,
    val doseMg: Double,
    val scheduledTimestamp: Long,
    val isTaken: Boolean?,
)

data class ScheduledIntakeDetails2(
    @Embedded val plannedIntake: PlannedIntakeEntity,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "id",
    )
    val course: MedicationCourseEntity,
    @Relation(
        entity = MedicationEntity::class,
        parentColumn = "courseId",
        entityColumn = "id",
        associateBy =
            Junction(
                value = MedicationCourseEntity::class,
                parentColumn = "id",
                entityColumn = "medicationId",
            ),
    )
    val medication: MedicationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "plannedIntakeId",
    )
    val intakeLog: IntakeLogEntity?,
)
