package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object Home : AppRoutes // расписание приемов.

    @Serializable
    data object Medications : AppRoutes

    @Serializable
    data object AddMedication : AppRoutes
}

