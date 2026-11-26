package com.galeria.medicationstracker.data.medications.mapper

import com.galeria.medicationstracker.core.database.entity.UserEntity
import com.galeria.medicationstracker.domain.auth.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    email = email,
    name = name,
    weight = weightKg,
    height = heightCm,
    dateOfBirth = dateOfBirth
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    name = name,
    weightKg = weight,
    heightCm = height,
    dateOfBirth = dateOfBirth
)
