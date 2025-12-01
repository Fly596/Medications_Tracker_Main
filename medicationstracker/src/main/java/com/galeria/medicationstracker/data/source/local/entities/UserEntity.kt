package com.galeria.medicationstracker.data.source.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    indices = [Index(value = ["firestoreId"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firestoreId: String,
    val name: String,
    val email: String,
    val weightKg: Double,
    val heightCm: Double,
    val dateOfBirth: Long,
)
