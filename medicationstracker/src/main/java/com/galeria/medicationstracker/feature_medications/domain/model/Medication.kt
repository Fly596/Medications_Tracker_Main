package com.galeria.medicationstracker.feature_medications.domain.model

data class Medication(
    val id: String,
    val name: String,
    val form: String,
    val stockCount: Double?,
    val measureUnit: String, // Показывает это штуки или граммы.
)
