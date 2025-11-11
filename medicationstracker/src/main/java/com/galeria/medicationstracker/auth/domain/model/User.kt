package com.galeria.medicationstracker.auth.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val weightKg: Float,
    val heightCm: Float,
    val dateOfBirth: Long
)
