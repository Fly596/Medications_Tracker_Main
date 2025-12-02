package com.galeria.medtracker2.feature_auth.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Instant,
)
