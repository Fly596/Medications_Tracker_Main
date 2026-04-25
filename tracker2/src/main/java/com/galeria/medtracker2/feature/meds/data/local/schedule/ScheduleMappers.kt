package com.galeria.medtracker2.feature.meds.data.local.schedule

import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain
import java.time.Instant

fun MedicationRegimentDomain.toEntity(): MedicationRegimenEntity {
    return MedicationRegimenEntity(
        id = this.id,
        medicationId = this.medicationId, // генерировать при создании.
        doseMg = this.doseMg,
        startDate = this.startDate,
        endDate = this.endDate,
    )
}

fun MedicationRegimenEntity.toDomain(): MedicationRegimentDomain {
    return MedicationRegimentDomain(
        id = this.id,
        medicationId = this.medicationId,
        doseMg = this.doseMg,
        startDate = this.startDate,
        endDate = this.endDate ?: Instant.MAX,
    )
}


