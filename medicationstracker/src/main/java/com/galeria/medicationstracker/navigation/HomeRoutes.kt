package com.galeria.medicationstracker.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreen(val route: String) {
    
    @Serializable object TodayMedications : HomeScreen("today_medications")
    
    @Serializable object MoodCheck : HomeScreen("mood_check")
}
