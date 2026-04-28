package com.galeria.medtracker2.feature.meds.data.local.combined

import java.util.UUID

data class FullSchedule(
    val idDateTime: UUID,
    val idRegiment: UUID,
    val name: String,
    val doseMg: Double,
    val scheduledIntakeDateTime: Long,
)
