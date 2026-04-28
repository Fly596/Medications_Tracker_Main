package com.galeria.medtracker2.feature.meds.data.local.medication

import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import java.time.Instant

fun MedicationDomain.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        creationDate = this.creationDate.toEpochMilli()
    )
}

fun MedicationEntity.toDomain(): MedicationDomain {
    return MedicationDomain(
        id = this.id,
        name = this.name,
        creationDate = Instant.ofEpochMilli(this.creationDate)
    )
}