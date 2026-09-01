package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.IntakeEntity
import com.galeria.medtracker2.domain.model.IntakeDomain

fun IntakeDomain.toEntity(): IntakeEntity {
    return IntakeEntity(
        medicationId = this.medicationId,
        amount = this.amount,
        unit = this.unit,
        cost = this.cost,
        intakeDateTime = this.intakeDateTime,
    )
}

fun IntakeEntity.toDomain(): IntakeDomain {
    return IntakeDomain(
        id = this.id,
        medicationId = this.medicationId,
        amount = this.amount,
        unit = this.unit,
        cost = this.cost,
        intakeDateTime = this.intakeDateTime,
    )
}