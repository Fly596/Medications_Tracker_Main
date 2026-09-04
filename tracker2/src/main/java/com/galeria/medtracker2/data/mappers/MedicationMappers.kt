package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.MedicationEntity
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.model.Money

fun MedicationDomain.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        unit = this.unit.name,
        defaultPriceCents = this.defaultPricePerUnit?.cents,
        currencyCode = this.defaultPricePerUnit?.currencyCode,
        creationTimestamp = this.creationTimestamp,
    )
}

fun MedicationEntity.toDomain(): MedicationDomain {
    return MedicationDomain(
        id = this.id,
        name = this.name,
        unit = WeightUnits.valueOf(this.unit),
        defaultPricePerUnit = defaultPriceCents?.let {
            Money(
                cents = it,
                currencyCode = currencyCode ?: "USD"
            )
        },
        creationTimestamp = this.creationTimestamp,
    )
}
