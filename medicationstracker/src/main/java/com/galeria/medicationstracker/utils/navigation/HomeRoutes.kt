package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreen(val route: String) {

    //@Serializable object Home : HomeScreen("home")

    @Serializable object TodayMedications : HomeScreen("today_medications")

    @Serializable object IntakeCheckDialog : HomeScreen("intake_check_dialog")

    @Serializable object MoodCheck : HomeScreen("mood_check")
}
