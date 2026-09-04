package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object Home : AppRoutes // список принимаемых лекарств.

    @Serializable
    data object AddMedication : AppRoutes

    @Serializable
    data object MedicationsList : AppRoutes

    @Serializable
    data class MedicationDetails(val medicationId: String) : AppRoutes

    @Serializable
    data class EditMedication(val medicationId: String) : AppRoutes

    @Serializable
    data object ProfileOverview : AppRoutes // стата по расходам и потреблению.
}
