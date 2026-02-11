package com.galeria.medicationstracker.feature_auth.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "user" )
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Instant,
)
