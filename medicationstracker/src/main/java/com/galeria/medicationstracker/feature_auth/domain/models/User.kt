package com.galeria.medicationstracker.feature_auth.domain.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String
)
