package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
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
