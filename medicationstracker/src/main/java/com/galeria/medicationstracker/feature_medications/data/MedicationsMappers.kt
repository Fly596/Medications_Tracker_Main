package com.galeria.medicationstracker.feature_medications.data

import com.galeria.medicationstracker.data.source.network.MedicationForm
import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationEntity
import com.galeria.medicationstracker.feature_medications.domain.model.Medication

fun MedicationEntity.toDomain(): Medication {
    return Medication(
        id = this.id,
        name = this.name,
        form = this.form.name,
        stockCount = this.stockCount,
        measureUnit = this.measureUnit)
}

fun Medication.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        form = MedicationForm.valueOf(this.form),
        stockCount = this.stockCount,
        measureUnit = this.measureUnit)
}
