package com.galeria.medicationstracker.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val weightKg: Float? = null,
    val heightCm: Float? = null,
    val dateOfBirth: Instant? = null,
)
