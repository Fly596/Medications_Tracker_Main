package com.galeria.medicationstracker.data.medications.mapper

import com.galeria.medicationstracker.core.database.entity.IntakeEntity
import com.galeria.medicationstracker.domain.medications.model.Intake

fun Intake.toEntity(): IntakeEntity = IntakeEntity(
    id = id,
    medicationId = medicationId,
    presetTime = presetTime,
    intakeTime = intakeTime,
    status = status
)

fun IntakeEntity.toDomain(): Intake = Intake(
    id = id,
    medicationId = medicationId,
    presetTime = presetTime,
    intakeTime = intakeTime,
    status = status
)