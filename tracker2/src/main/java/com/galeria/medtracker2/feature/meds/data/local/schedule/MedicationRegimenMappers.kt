package com.galeria.medtracker2.feature.meds.data.local.schedule

import com.galeria.medtracker2.feature.meds.domain.ScheduledDomain

fun ScheduledDomain.toEntity(): ScheduledDateTimeEntity {
    return ScheduledDateTimeEntity(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        scheduledIntakeDateTime = this.scheduledIntakeDateTime
    )
}

fun ScheduledDateTimeEntity.toDomain(): ScheduledDomain {
    return ScheduledDomain(
        id = this.id,
        medicationScheduleId = this.medicationScheduleId,
        scheduledIntakeDateTime = this.scheduledIntakeDateTime
    )
}