package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable data object Home : AppRoutes // расписание приемов на сегодня.

    @Serializable
    data object AddMedicationRoute : AppRoutes

    @Serializable
    data object MedicationsListRoute : AppRoutes // Мои приемы

    @Serializable
    data class MedicationDetailsRoute(val medicationId: String) : AppRoutes


}

