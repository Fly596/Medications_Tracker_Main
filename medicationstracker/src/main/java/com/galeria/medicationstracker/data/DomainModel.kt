package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.local.Dosage
import com.galeria.medicationstracker.data.network.IntakeStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class DomainDosage(
    val value: Double,
    val unit: String,
)

data class DomainMedication(
    val localId: Int,
    val firestoreId: String, // Полезно иметь и networkId в доменной модели..
    val name: String,
    val dosage: DomainDosage, // Используем тот же вложенный класс..
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val daysOfWeek: List<String>,
    val intakeTime: Int,
)

data class DomainUser(
    val localId: Int,
    val firestoreId: String,
    val name: String,
    val email: String,
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val dateOfBirth: LocalDate?
)

data class DomainIntake(
    val localId: Int,
    val firestoreId: String,
    val medicationFirestoreId: String,
    val status: IntakeStatus,
    val presetMinutesFromMidnight: Int,
    val factTimestamp: LocalDateTime?
)