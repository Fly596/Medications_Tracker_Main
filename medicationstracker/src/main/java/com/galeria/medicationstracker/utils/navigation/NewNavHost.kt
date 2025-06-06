package com.galeria.medicationstracker.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Composable
fun ApplicationNavHost(
    startDestination: Any = Routes.Auth,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authGraph(navController)
        // dashboardGraph(navController)
        // medicationsGraph(navController)
        // profileGraph(navController)
        // userMedsGraph(navController)
    }
}

@Serializable
sealed class Routes(val route: String) {

    @Serializable data object Auth : Routes("auth")

    @Serializable data object Home : Routes("home")

    @Serializable data object Medications : Routes("medications")

    @Serializable data object PatientDashboard : Routes("patient_dashboard")
}
