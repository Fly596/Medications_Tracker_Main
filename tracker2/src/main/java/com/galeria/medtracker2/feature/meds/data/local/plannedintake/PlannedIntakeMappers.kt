package com.galeria.medtracker2.feature.meds.data.local.plannedintake

import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import java.time.Instant

fun PlannedIntakeDomain.toEntity(): PlannedIntakeEntity {
    return PlannedIntakeEntity(
        id = this.id,
        medicationScheduleId = this.medicationCourseId,
        scheduledIntakeDateTime = this.scheduledIntakeDateTime.toEpochMilli()
    )
}

fun PlannedIntakeEntity.toDomain(): PlannedIntakeDomain {
    return PlannedIntakeDomain(
        id = this.id,
        medicationCourseId = this.medicationScheduleId,
        scheduledIntakeDateTime = Instant.ofEpochMilli(this.scheduledIntakeDateTime)
    )
}