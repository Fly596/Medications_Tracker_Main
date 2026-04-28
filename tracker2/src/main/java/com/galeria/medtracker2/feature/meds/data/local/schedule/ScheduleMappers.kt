package com.galeria.medtracker2.feature.meds.data.local.schedule

import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain

fun MedicationCourseDomain.toEntity(): MedicationCourseEntity {
    return MedicationCourseEntity(
        id = this.id,
        medicationId = this.medicationId, // генерировать при создании.
        doseMg = this.doseMg,
        startDate = this.startDate.toEpochMilli(),
        endDate = this.endDate.toEpochMilli(),
    )
}

fun MedicationCourseEntity.toDomain(): MedicationCourseDomain {
    return MedicationCourseDomain(
        id = this.id,
        medicationId = this.medicationId,
        doseMg = this.doseMg,
        startDate = java.time.Instant.ofEpochMilli(this.startDate),
        endDate = java.time.Instant.ofEpochMilli(this.endDate!!),
    )
}
