package com.galeria.medicationstracker.feature_medications.data

import com.galeria.medicationstracker.feature_medications.data.source.local.RegimentsEntity
import com.galeria.medicationstracker.feature_medications.data.source.remote.model.RegimentsDto
import com.galeria.medicationstracker.feature_medications.domain.model.FrequencyType
import com.galeria.medicationstracker.feature_medications.domain.model.Regiments
import com.google.firebase.Timestamp
import java.time.Instant
import java.util.Date
import java.util.UUID

// data/mapper/RegimentEntityMapper.kt
fun RegimentsEntity.toDomain(): Regiments = Regiments(
    id = id,
    medicationId = medicationId,
    startDate = java.time.Instant.ofEpochMilli(startDate),
    endDate = endDate?.let { Instant.ofEpochMilli(it) } as Instant,
    frequencyType = FrequencyType.safeValueOf(frequencyType),
    frequencyDetails = frequencyDetails,
    // Парсим ISO строки обратно в Instant
    timeSlots = timeSlots.map { Instant.parse(it) },
    dosage = dosage
)

fun Regiments.toEntity(): RegimentsEntity = RegimentsEntity(
    id = id.ifBlank { UUID.randomUUID().toString() },
    medicationId = medicationId,
    startDate = startDate.toEpochMilli(),
    endDate = endDate?.toEpochMilli(),
    frequencyType = frequencyType.name,
    frequencyDetails = frequencyDetails,
    // Храним Instant как ISO-8601 строки для читаемости в БД или Long
    timeSlots = timeSlots.map { it.toString() },
    dosage = dosage
)

// data/mapper/RegimentDtoMapper.kt
fun RegimentsDto.toDomain(): Regiments = Regiments(
    id = id,
    medicationId = medicationId,
    startDate = startDate.toDate().toInstant(),
    endDate = endDate?.toDate()?.toInstant(),
    frequencyType = FrequencyType.safeValueOf(frequencyType),
    frequencyDetails = frequencyDetails,
    timeSlots = timeSlots.map { it.toDate().toInstant() },
    dosage = dosage
)

fun Regiments.toDto(): RegimentsDto = RegimentsDto(
    id = id,
    medicationId = medicationId,
    startDate = Timestamp(Date.from(startDate)),
    endDate = endDate?.let { Timestamp(Date.from(it)) },
    frequencyType = frequencyType.name,
    frequencyDetails = frequencyDetails,
    timeSlots = timeSlots.map { Timestamp(Date.from(it)) },
    dosage = dosage
)