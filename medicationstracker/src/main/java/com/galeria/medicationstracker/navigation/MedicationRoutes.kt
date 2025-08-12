package com.galeria.medicationstracker.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MedicationScreen(val route: String) {

    @Serializable object MedicationsList : MedicationScreen("medications")

    @Serializable object AddMedication : MedicationScreen("add_medication")

    @Serializable
    data class ViewMedication(val medicationId: String) : MedicationScreen("view_medication")
    
    @Serializable
    data class UpdateMedication(val medicationId: String) :
        MedicationScreen("update_medication")
}
