package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.database.entity.UserEntity
import com.galeria.medicationstracker.core.firebase.model.MedicationDocument
import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.galeria.medicationstracker.data.medications.mapper.toEntity
import com.galeria.medicationstracker.domain.auth.model.User
import com.galeria.medicationstracker.domain.medications.model.Medication
import com.galeria.medicationstracker.utils.toTimestamp
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

// --- User Mappers ---

// DTO -> Entity
fun UserDocument.toEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    name = name,
    weightKg = weight,
    heightCm = height,
    dateOfBirth = dateOfBirth?.toInstant()
)

// Domain -> DTO
fun User.toDocument(): UserDocument = UserDocument(
    id = id,
    email = email,
    name = name,
    weight = weight,
    height = height,
    dateOfBirth = dateOfBirth?.toTimestamp()
)

// Entity -> Domain

// --- Medication Mappers ---

// Domain -> Room Entity

// Firebase Document -> Domain
fun MedicationDocument.toDomain(): Medication {
    val zone = ZoneId.systemDefault()
    val startLocalDate = startDate?.let {
        Instant.ofEpochMilli(it.seconds * 1000).atZone(zone).toLocalDate()
    }

    val endLocalDate = endDate?.let {
        Instant.ofEpochMilli(it.seconds * 1000).atZone(zone).toLocalDate()
    }

    return Medication(
        id = id,
        name = name,
        dosage = dosage,
        form = runCatching { MedicationForm.valueOf(form) }.getOrDefault(MedicationForm.UNKNOWN),
        startDate = startLocalDate,
        endDate = endLocalDate,
        daysOfWeek = daysOfWeek,
        intakeTime = LocalTime.ofSecondOfDay(intakeTimeInSeconds.toLong())
    )
}

// Firebase Document -> Room Entity
fun MedicationDocument.toEntity(): MedicationEntity = toDomain().toEntity()

// Domain -> Firebase Document
fun Medication.toDocument(): MedicationDocument {
    val zone = ZoneId.systemDefault()
    val startTimestamp = startDate?.let { Timestamp(Date.from(it.atStartOfDay(zone).toInstant())) }
    val endTimestamp = endDate?.let { Timestamp(Date.from(it.atStartOfDay(zone).toInstant())) }

    return MedicationDocument(
        id = id,
        name = name,
        dosage = dosage,
        form = form.name,
        startDate = startTimestamp,
        endDate = endTimestamp,
        daysOfWeek = daysOfWeek,
        intakeTimeInSeconds = intakeTime.toSecondOfDay()
    )
}
