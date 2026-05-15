package com.galeria.medtracker2.feature.meds.data.local.plannedintake

import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import java.time.Instant

fun PlannedIntakeDomain.toEntity(): PlannedIntakeEntity {
    return PlannedIntakeEntity(
        id = this.id,
        courseId = this.courseId,
        scheduledTimestamp = this.scheduledTimestamp.toEpochMilli(),
    )
}

fun PlannedIntakeEntity.toDomain(): PlannedIntakeDomain {
    return PlannedIntakeDomain(
        id = this.id,
        courseId = this.courseId,
        scheduledTimestamp = Instant.ofEpochMilli(this.scheduledTimestamp),
    )
}
