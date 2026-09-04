package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.IntakeEntity
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.domain.model.Dose
import com.galeria.medtracker2.domain.model.IntakeDomain
import com.galeria.medtracker2.domain.model.Money

fun IntakeDomain.toEntity(): IntakeEntity {
    return IntakeEntity(
        medicationId = this.medicationId,
        amount = this.dose.amount,
        unit = this.dose.unit.name,
        priceCents = this.cost?.cents,
        currencyCode = this.cost?.currencyCode,
        timestamp = this.intakeDateTime,
    )
}

fun IntakeEntity.toDomain(): IntakeDomain {
    return IntakeDomain(
        id = this.id,
        medicationId = this.medicationId,
        dose = Dose(
            amount = this.amount,
            unit = WeightUnits.valueOf(this.unit)
        ),
        cost = this.priceCents?.let {
            Money(
                cents = it,
                currencyCode = this.currencyCode ?: "USD"
            )
        },
        intakeDateTime = this.timestamp,

        )
}