package com.galeria.medicationstracker.feature_medications.domain.model

import com.galeria.medicationstracker.feature_medications.data.source.local.FrequencyType
import java.time.LocalDateTime

data class Regiments(
    val id: String = "",
    val medicationId: String= "",
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val frequencyType: FrequencyType,
    val frequencyDetails: String?,
    val timeSlots: List<LocalDateTime>,
    val dosage: Double,

    
    )