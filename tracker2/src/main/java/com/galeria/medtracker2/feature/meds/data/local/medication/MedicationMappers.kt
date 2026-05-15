package com.galeria.medtracker2.feature.meds.data.local.medication

import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
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
