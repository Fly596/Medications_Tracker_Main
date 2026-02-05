package com.galeria.medicationstracker.feature_medications.domain.model

import java.time.Instant

data class Regiments(
    val id: String = "",
    val medicationId: String = "",
    val startDate: Instant,
    val endDate: Instant?,
    val frequencyType: FrequencyType,
    val frequencyDetails: String?,
    val timeSlots: List<Instant>,
    val dosage: Double,
)

// domain/model/FrequencyType.kt
enum class FrequencyType {
    
    DAILY, SPECIFIC_DAYS, INTERVAL;
    
    companion object {
        
        fun safeValueOf(value: String): FrequencyType =
            runCatching { valueOf(value) }.getOrDefault(DAILY)
    }
}
