package com.galeria.medicationstracker.domain.model

import kotlin.time.Instant

data class NoteDomain(
    val id: Int,
    val userId: Int,
    val content: String,
    val tags: List<String>,
    val timestamp: Instant,
)
