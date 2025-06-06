package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthScreen(val route: String) {

    @Serializable data object Login : AuthScreen("login")

    @Serializable data object Registration : AuthScreen("registration")

    @Serializable data object PasswordRecovery : AuthScreen("password_recovery")
}
