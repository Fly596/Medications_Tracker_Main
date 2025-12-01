package com.galeria.medicationstracker.feature_auth.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.time.Instant

@Entity(
    tableName = "user"/* ,
    indices = [Index(value = ["firestoreId"], unique = true)] */)
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Instant,
)
