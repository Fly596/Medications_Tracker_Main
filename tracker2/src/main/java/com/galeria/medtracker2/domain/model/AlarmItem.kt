package com.galeria.medtracker2.domain.model

import java.util.UUID

data class AlarmItem(
    val id: UUID,
    val timeMillis: Long,
    val title: String,
    val message: String
)
