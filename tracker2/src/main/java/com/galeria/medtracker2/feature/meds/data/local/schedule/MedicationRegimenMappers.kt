package com.galeria.medtracker2.feature.meds.data.local.schedule

import com.galeria.medtracker2.feature.meds.domain.ScheduledDateTimeDomain

fun ScheduledDateTimeDomain.toEntity(): ScheduledDateTimeEntity {
    return ScheduledDateTimeEntity(
        id = this.id,
        medicationScheduleId = this.medicationRegimentId,
        scheduledIntakeDateTime = this.scheduledIntakeDateTime.toEpochMilli()
    )
}

fun ScheduledDateTimeEntity.toDomain(): ScheduledDateTimeDomain {
    return ScheduledDateTimeDomain(
        id = this.id,
        medicationRegimentId = this.medicationScheduleId,
        scheduledIntakeDateTime = java.time.Instant.ofEpochMilli(this.scheduledIntakeDateTime)
    )
}