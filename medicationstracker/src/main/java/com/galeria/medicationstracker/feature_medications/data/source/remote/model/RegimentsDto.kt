package com.galeria.medicationstracker.feature_medications.data.source.remote.model

import com.google.firebase.Timestamp

data class RegimentsDto(
    val id: String = "",
    val medicationId: String = "",
    val startDate: Timestamp,
    val endDate: Timestamp?, // null - вечно.
    val frequencyType: String,
    // Если SPECIFIC_DAYS -> [MONDAY, WEDNESDAY].
    // Если INTERVAL -> 2 (раз в 2 дня).
    val frequencyDetails: String?,
    // Время приемов в течение дня (JSON List<LocalTime>).
    // Например: ["08:00", "14:00", "20:00"].
    val timeSlots: List<Timestamp>,
    val dosage: Double,
)