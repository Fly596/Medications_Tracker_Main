package com.galeria.medtracker2.feature.intakes.data.local

import com.galeria.medtracker2.feature.intakes.domain.IntakeLogDomain
import java.time.Instant

fun IntakeLogDomain.toEntity(): IntakeLogEntity {
    return IntakeLogEntity(
        id = this.id,
        plannedIntakeId = this.plannedIntakeId,
        actualTimestamp = this.actualTimestamp.toEpochMilli(),
        isTaken = this.isTaken,
        notes = this.notes,
    )
}

fun IntakeLogEntity.toDomain(): IntakeLogDomain {
    return IntakeLogDomain(
        id = this.id,
        plannedIntakeId = this.plannedIntakeId,
        actualTimestamp = Instant.ofEpochMilli(this.actualTimestamp),
        isTaken = this.isTaken,
        notes = this.notes ?: "",
    )
}
