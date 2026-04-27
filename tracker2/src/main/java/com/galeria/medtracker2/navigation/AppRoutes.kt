package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object Home : AppRoutes // расписание приемов на сегодня.

    @Serializable
    data object IntakesHistory : AppRoutes

    @Serializable
    data object Medications : AppRoutes // Мои приемы

    @Serializable
    data object AddMedication : AppRoutes
}

