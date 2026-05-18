package com.galeria.medtracker2.data.mappers

import com.galeria.medtracker2.core.database.entity.MedicationEntity
import com.galeria.medtracker2.domain.model.MedicationDomain
import java.time.Instant

fun MedicationDomain.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        creationTimestamp = this.creationTimestamp.toEpochMilli(),
    )
}

fun MedicationEntity.toDomain(): MedicationDomain {
    return MedicationDomain(
        id = this.id,
        name = this.name,
        creationTimestamp = Instant.ofEpochMilli(this.creationTimestamp),
    )
}
