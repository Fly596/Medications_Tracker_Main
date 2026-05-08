package com.galeria.medtracker2.core.common.data

import java.util.UUID

data class FullSchedule(
    val idDateTime: UUID,
    val idRegiment: UUID,
    val medName: String,
    val doseMg: Double,
    val scheduledIntakeDateTime: Long,
    val status: Boolean?
)