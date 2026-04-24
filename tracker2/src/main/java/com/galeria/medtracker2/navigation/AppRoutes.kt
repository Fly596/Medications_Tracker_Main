package com.galeria.medtracker2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object Home : AppRoutes

    @Serializable
    data object Medications : AppRoutes

    @Serializable
    data object AddMedication : AppRoutes
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoutes.Home
    ) {

    }
}