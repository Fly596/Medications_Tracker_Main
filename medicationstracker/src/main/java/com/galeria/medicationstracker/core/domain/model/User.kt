package com.galeria.medicationstracker.core.domain.model

import java.time.Instant

data class User(
  val id: String,
  val name: String,
  val email: String,
  val weight: Float?,
  val height: Float?,
  val dateOfBirth: Instant?,
)
