package com.galeria.medicationstracker.data.old

/* sealed class UserIntakeStatus {
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
} */

enum class MedicationUnit {
    MG,
    MCG,
    G,
    ML,
    OZ,
}
