package com.galeria.medicationstracker.feature_auth.data.source.network

import com.google.firebase.Timestamp

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Timestamp,
)
