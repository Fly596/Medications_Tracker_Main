package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthScreen {

    @Serializable data object Login : AuthScreen

    @Serializable data object Registration : AuthScreen

    @Serializable data object PasswordRecovery : AuthScreen
}
