package com.galeria.medtracker2.feature.meds.data.local.course

import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain
import java.time.Instant

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
        startDate = Instant.ofEpochMilli(this.startDate),
        endDate = Instant.ofEpochMilli(this.endDate!!),
    )
}
