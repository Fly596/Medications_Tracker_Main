package com.galeria.medtracker2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galeria.medtracker2.feature.meds.presentation.AddMedicationScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoutes.AddMedication
    ) {
        composable<AppRoutes.Home> {
            //
        }
        composable<AppRoutes.AddMedication> {
            AddMedicationScreen()
        }

    }
}