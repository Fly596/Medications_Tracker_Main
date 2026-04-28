package com.galeria.medtracker2.feature.meds.data.local.schedule

import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain

fun MedicationRegimentDomain.toEntity(): MedicationRegimenEntity {
    return MedicationRegimenEntity(
        id = this.id,
        medicationId = this.medicationId, // генерировать при создании.
        doseMg = this.doseMg,
        startDate = this.startDate.toEpochMilli(),
        endDate = this.endDate.toEpochMilli(),
    )
}

fun MedicationRegimenEntity.toDomain(): MedicationRegimentDomain {
    return MedicationRegimentDomain(
        id = this.id,
        medicationId = this.medicationId,
        doseMg = this.doseMg,
        startDate = java.time.Instant.ofEpochMilli(this.startDate),
        endDate = java.time.Instant.ofEpochMilli(this.endDate!!),
    )
}
