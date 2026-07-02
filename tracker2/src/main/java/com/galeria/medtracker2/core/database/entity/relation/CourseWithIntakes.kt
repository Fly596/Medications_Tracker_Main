package com.galeria.medtracker2.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.galeria.medtracker2.core.database.entity.MedicationCourseEntity
import com.galeria.medtracker2.core.database.entity.MedicationEntity
import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity

data class CourseWithIntakes(
    @Embedded val course: MedicationCourseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "courseId",
    )
    val intakes: List<PlannedIntakeEntity>,
    @Relation(
        parentColumn = "medicationId",
        entityColumn = "id",
    )
    val medication: MedicationEntity,
)
