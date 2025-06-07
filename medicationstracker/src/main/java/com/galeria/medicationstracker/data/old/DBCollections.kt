package com.galeria.medicationstracker.data.old
/*
data class Interaction(
    val medication1: String = "",
    val medication2: String = "",
    val effect: String = "",
    val severity: String = "",
)

sealed class InteractionType(val severity: String) {
    object Major : InteractionType("Высокий риск")

    object Moderate : InteractionType("Средний риск")

    object Minor : InteractionType("Низкий риск")

    object Unknown : InteractionType("Неизвестно")

    companion object {

        fun fromString(value: String): InteractionType {
            return when (value.uppercase()) {
                "MAJOR" -> Major
                "MODERATE" -> Moderate
                "MINOR" -> Minor
                else -> Unknown
            }
        }
    }
} */

sealed class UserIntakeStatus {
    object Taken : UserIntakeStatus() // Принято

    object Skipped : UserIntakeStatus() // Пропущено

    object Pending : UserIntakeStatus() // Ожидает приема

    companion object {

        fun fromBoolean(status: Boolean?): UserIntakeStatus {
            return when (status) {
                true -> Taken
                false -> Skipped
                null -> Pending
            }
        }
    }
}

enum class MedicationUnit {
    MG,
    MCG,
    G,
    ML,
    OZ,
}
