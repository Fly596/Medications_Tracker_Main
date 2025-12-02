package com.galeria.medtracker2.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Graph {

    @Serializable data object Auth : Graph

    @Serializable data object App : Graph
}

// --- Уровень 2: Маршруты Экранов (Сгруппированные) ---
@Serializable
sealed interface AuthScreen {

    @Serializable data object Login : AuthScreen

    @Serializable data object Registration : AuthScreen

    @Serializable data object RestorePassword : AuthScreen
}

@Serializable
sealed interface AppScreen {

    @Serializable data class Home(val userId: String = "") : AppScreen

    @Serializable data object Medications : AppScreen

    @Serializable data object Account : AppScreen
}
