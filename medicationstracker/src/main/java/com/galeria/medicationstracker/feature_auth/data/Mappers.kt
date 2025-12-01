package com.galeria.medicationstracker.feature_auth.data

import com.galeria.medicationstracker.feature_auth.data.source.local.UserEntity
import com.galeria.medicationstracker.feature_auth.data.source.network.UserDto
import com.galeria.medicationstracker.feature_auth.domain.UserDomain

// Превращаем Domain в Entity для Room
fun UserDomain.toEntity(): UserEntity {
    return UserEntity(
        id = this.id, // ID должен приходить извне (Firebase UID)
        name = this.name,
        email = this.email,
        weightKg = this.weightKg,
        heightCm = this.heightCm,
        dateOfBirth = this.dateOfBirth
    )
}

// Превращаем Domain в DTO (или Map) для Firestore
// Firestore удобно принимает обычные Data Class, если поля совпадают
fun UserDomain.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        email = this.email,
        weightKg = this.weightKg,
        heightCm = this.heightCm,
        dateOfBirth = this.dateOfBirth.toString() // Firestore не умеет хранить Instant напрямую в JSON, лучше String ISO
    )
}