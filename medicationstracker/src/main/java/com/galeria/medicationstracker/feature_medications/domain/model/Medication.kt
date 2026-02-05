package com.galeria.medicationstracker.feature_medications.domain.model

data class Medication(
    val id: String,
    val name: String,
    val form: MedicationForm,
    val stockCount: Double?,
    val measureUnit: String, // Показывает это штуки или граммы.
    val drugClass: String, // stim/opioid/benz..
)

enum class MedicationForm {
    TABLET,
    CAPSULE,
    SYRUP,
    INJECTION,
    UNKNOWN,
    LIQUID,
    POWDER;
    
    companion object {
        
        fun safelyFrom(value: String): MedicationForm =
            entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}
