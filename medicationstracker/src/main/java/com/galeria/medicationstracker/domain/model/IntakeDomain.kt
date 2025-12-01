package com.galeria.medicationstracker.domain.model

import kotlin.time.Instant

data class IntakeDomain(
    val id: Int,
    val medicationId: Int,
    val status: String,
    val timestamp: Instant
)
