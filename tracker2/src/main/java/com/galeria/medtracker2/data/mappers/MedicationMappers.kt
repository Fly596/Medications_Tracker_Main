package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.MedicationEntity
import com.galeria.medtracker2.domain.model.MedicationDomain

fun MedicationDomain.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        pricing = this.pricing,
        unit = this.unit,
        creationTimestamp = this.creationTimestamp,
    )
}

fun MedicationEntity.toDomain(): MedicationDomain {
    return MedicationDomain(
        id = this.id,
        name = this.name,
        pricing = this.pricing,
        unit = this.unit,
        creationTimestamp = this.creationTimestamp,
    )
}
