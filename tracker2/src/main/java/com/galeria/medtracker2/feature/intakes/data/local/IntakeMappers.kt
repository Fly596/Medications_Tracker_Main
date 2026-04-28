package com.galeria.medtracker2.feature.intakes.data.local

import com.galeria.medtracker2.feature.intakes.domain.IntakeLogDomain
import java.time.Instant

fun IntakeLogDomain.toEntity(): IntakeLogEntity {
    return IntakeLogEntity(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        actualIntakeDateTime = this.actualIntakeDateTime.toEpochMilli(),
        status = this.status,
        notes = this.notes
    )
}

fun IntakeLogEntity.toDomain(): IntakeLogDomain {
    return IntakeLogDomain(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        actualIntakeDateTime = Instant.ofEpochMilli(this.actualIntakeDateTime),
        status = this.status,
        notes = this.notes ?: ""
    )
}