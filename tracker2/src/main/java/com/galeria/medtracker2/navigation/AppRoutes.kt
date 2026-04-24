package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Graph {

    @Serializable
    data object App : Graph
}

@Serializable
sealed interface AppScreen {

    @Serializable
    data object Home : AppScreen

    @Serializable
    data object Medications : AppScreen

    @Serializable
    data object Account : AppScreen
}
