package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthScreen {
    
    @Serializable
    data object Login : AuthScreen
    
    @Serializable
    data class Registration(val email: String) : AuthScreen
    
    @Serializable
    data class PasswordRecovery(val email: String) : AuthScreen
}
