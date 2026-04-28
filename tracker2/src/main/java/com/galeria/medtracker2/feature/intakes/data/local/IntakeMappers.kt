package com.galeria.medtracker2.feature.intakes.data.local

import com.galeria.medtracker2.feature.intakes.domain.IntakeDomain
import java.time.Instant

fun IntakeDomain.toEntity(): IntakeEntity {
    return IntakeEntity(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        actualIntakeDateTime = this.actualIntakeDateTime.toEpochMilli(),
        status = this.status,
        notes = this.notes
    )
}

fun IntakeEntity.toDomain(): IntakeDomain {
    return IntakeDomain(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        actualIntakeDateTime = Instant.ofEpochMilli(this.actualIntakeDateTime),
        status = this.status,
        notes = this.notes ?: ""
    )
}