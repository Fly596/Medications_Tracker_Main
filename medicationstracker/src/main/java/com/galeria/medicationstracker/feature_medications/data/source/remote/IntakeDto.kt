package com.galeria.medicationstracker.feature_medications.data.source.remote

data class IntakeDto(
    val id: String,
    val scheduledForDate: String,
    val scheduledForTime: String?,
    val takenAt: String,
    val status: String,
    val actualDosage: Double,
)
