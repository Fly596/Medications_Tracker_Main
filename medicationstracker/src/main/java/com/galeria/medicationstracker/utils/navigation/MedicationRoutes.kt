package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MedicationScreen(val route: String) {

    @Serializable object MedicationsList : MedicationScreen("medications")

    @Serializable object AddMedication : MedicationScreen("add_medication")

    @Serializable
    data class ViewMedication(val medicationId: String) : MedicationScreen("view_medication")

    @Serializable object UpdateMedication : MedicationScreen("update_medication")
}
