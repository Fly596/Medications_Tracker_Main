package com.galeria.medtracker2.feature.auth.domain.model

import kotlin.time.Instant

data class UserDomain(
    val id: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Instant,
)