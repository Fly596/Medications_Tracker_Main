package com.galeria.medtracker2.feature.meds.data.local.intakes

import com.galeria.medtracker2.feature.meds.domain.IntakeDomain

fun IntakeDomain.toEntity(): IntakeEntity {
    return IntakeEntity(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        actualIntakeDateTime = this.actualIntakeDateTime.toEpochMilli(),
        notes = this.notes
    )
}

fun IntakeEntity.toDomain(): IntakeDomain {
    return IntakeDomain(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        actualIntakeDateTime = java.time.Instant.ofEpochMilli(this.actualIntakeDateTime),
        notes = this.notes ?: ""
    )
}