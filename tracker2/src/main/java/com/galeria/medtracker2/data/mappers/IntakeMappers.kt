package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.IntakeLogEntity
import com.galeria.medtracker2.domain.model.IntakeLogDomain
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
