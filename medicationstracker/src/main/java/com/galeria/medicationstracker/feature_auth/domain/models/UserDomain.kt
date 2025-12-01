package com.galeria.medicationstracker.feature_auth.domain.models

import kotlin.time.Instant

data class UserDomain(
    val id: Int,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Instant,
)
