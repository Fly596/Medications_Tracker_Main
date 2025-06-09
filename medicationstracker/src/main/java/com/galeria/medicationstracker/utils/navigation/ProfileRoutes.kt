package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ProfileScreen(val route: String) {
    
    @Serializable
    object ProfileMain : ProfileScreen("profile")

    @Serializable object ProfileOverview : ProfileScreen("profile_overview")

    @Serializable object Notes : ProfileScreen("notes")

    @Serializable object NewNote : ProfileScreen("new_note")

    @Serializable object Settings : ProfileScreen("settings")
}
