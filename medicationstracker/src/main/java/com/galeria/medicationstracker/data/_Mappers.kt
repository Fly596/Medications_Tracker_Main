package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.core.database.entity.UserEntity
import com.galeria.medicationstracker.core.domain.model.User
import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.galeria.medicationstracker.utils.toTimestamp

// DTO -> Entity
fun UserDocument.toEntity(): UserEntity = UserEntity(
  id = id,
  email = email,
  name = name,
  weight = weight,
  height = height,
  dateOfBirth = dateOfBirth?.toInstant()
)

// Domain -> DTO
fun User.toDocument(): UserDocument = UserDocument(
  id = id,
  email = email,
  name = name,
  weight = weight,
  height = height,
  dateOfBirth = dateOfBirth?.toTimestamp()
)

// Entity -> Domain
fun UserEntity.toDomain(): User = User(
  id = id,
  email = email,
  name = name,
  weight = weight,
  height = height,
  dateOfBirth = dateOfBirth
)