package com.galeria.medicationstracker.feature_medications.data

import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationEntity
import com.galeria.medicationstracker.feature_medications.data.source.remote.model.MedicationDto
import com.galeria.medicationstracker.feature_medications.domain.model.Medication
import com.galeria.medicationstracker.feature_medications.domain.model.MedicationForm

fun MedicationEntity.toDomain(): Medication {
    return Medication(
        id = this.id,
        name = this.name,
        form = this.form,
        stockCount = this.stockCount,
        measureUnit = this.measureUnit,
        drugClass = this.drugClass)
}

fun Medication.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        form = this.form,
        stockCount = this.stockCount,
        measureUnit = this.measureUnit,
        drugClass = this.drugClass)
}

fun MedicationDto.toDomain() =
    Medication(
        id = id,
        name = name,
        form = MedicationForm.safelyFrom(form),
        stockCount = stockCount,
        measureUnit = measureUnit,
        drugClass = drugClass
    )

fun Medication.toDto() =
    MedicationDto(
        id = id,
        name = name,
        form = form.name,
        stockCount = stockCount,
        measureUnit = measureUnit,
        drugClass = drugClass)
