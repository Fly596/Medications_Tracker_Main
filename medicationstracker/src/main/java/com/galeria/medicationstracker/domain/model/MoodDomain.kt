package com.galeria.medicationstracker.domain.model

import kotlin.time.Instant

data class MoodDomain(
    val id: Int,
    val moodValue: Int,
    val note: String,
    val timestamp: Instant
)
