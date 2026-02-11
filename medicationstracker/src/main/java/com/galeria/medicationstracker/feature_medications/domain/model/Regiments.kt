package com.galeria.medicationstracker.feature_medications.domain.model

import java.time.Instant

data class Regiments(
    val id: String = "",
    val medicationId: String = "",
    val startDate: Instant,
    val endDate: Instant?,
    val frequencyType: FrequencyType,
    val frequencyDetails: String?,
    val timeSlots: List<Int>,
    val dosage: Double,
)

// domain/model/FrequencyType.kt
enum class FrequencyType {
    
    DAILY, SPECIFIC_DAYS, INTERVAL;
    
    companion object {
        
        val time = kotlinx.datetime.LocalTime(6, 50, 0, 0)
        fun safeValueOf(value: String): FrequencyType =
            runCatching { valueOf(value) }.getOrDefault(DAILY)
    }
}
