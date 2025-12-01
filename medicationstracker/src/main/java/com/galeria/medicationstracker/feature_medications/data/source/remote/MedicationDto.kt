package com.galeria.medicationstracker.feature_medications.data.source.remote

data class MedicationDto(
    val id: String,
    val name: String,
    val form: String,
    val stockCount: Double,
    val measureUnit: String,
)
