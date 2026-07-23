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
  val weight: Float?,
  val height: Float?,
  val dateOfBirth: Instant?,
)
