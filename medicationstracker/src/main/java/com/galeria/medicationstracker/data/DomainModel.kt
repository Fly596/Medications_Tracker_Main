package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.local.Dosage
import java.time.LocalDate

data class DomainMedication(
    val id: Int,
    val networkId: String, // Полезно иметь и networkId в доменной модели..
    val name: String,
    val dosage: Dosage, // Используем тот же вложенный класс..
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val daysOfWeek: List<String>,
    val intakeTime: Int,
)