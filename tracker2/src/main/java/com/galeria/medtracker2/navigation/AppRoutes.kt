package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object Home : AppRoutes // расписание приемов на сегодня.

    @Serializable
    data object AddMedicationSchedule : AppRoutes

    @Serializable
    data object AddMedication : AppRoutes

    @Serializable
    data object MedicationsList : AppRoutes // Мои приемы

    @Serializable
    data class MedicationDetails(val medicationId: String) : AppRoutes

    @Serializable
    data class EditMedication(val medicationId: String) : AppRoutes
}
