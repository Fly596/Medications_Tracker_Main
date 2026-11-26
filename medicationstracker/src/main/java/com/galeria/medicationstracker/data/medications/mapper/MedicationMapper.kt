package com.galeria.medicationstracker.data.medications.mapper

import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.domain.medications.model.Medication

fun Medication.toEntity(): MedicationEntity = MedicationEntity(
    id = id,
    name = name,
    dosage = dosage,
    form = form,
    startDate = startDate,
    endDate = endDate,
    daysOfWeek = daysOfWeek,
    intakeTime = intakeTime
)

// Room Entity -> Domain
fun MedicationEntity.toDomain(): Medication = Medication(
    id = id,
    name = name,
    dosage = dosage,
    form = form,
    startDate = startDate,
    endDate = endDate,
    daysOfWeek = daysOfWeek,
    intakeTime = intakeTime
)
