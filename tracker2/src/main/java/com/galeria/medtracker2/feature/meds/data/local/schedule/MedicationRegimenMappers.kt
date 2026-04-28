package com.galeria.medtracker2.feature.meds.data.local.schedule

import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain

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
        scheduledIntakeDateTime = java.time.Instant.ofEpochMilli(this.scheduledIntakeDateTime)
    )
}