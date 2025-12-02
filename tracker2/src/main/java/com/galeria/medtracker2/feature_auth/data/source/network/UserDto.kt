package com.galeria.medtracker2.feature_auth.data.source.network

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: String,
)
