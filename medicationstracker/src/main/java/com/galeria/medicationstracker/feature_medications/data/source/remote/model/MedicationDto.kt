package com.galeria.medicationstracker.feature_medications.data.source.remote.model

data class MedicationDto(
    val id: String = "",
    val name: String = "",
    val form: String = "",
    val stockCount: Double? = null,
    val measureUnit: String = "",
    val drugClass: String = ""
)
