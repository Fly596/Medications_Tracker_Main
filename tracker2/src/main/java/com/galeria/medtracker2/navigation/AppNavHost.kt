package com.galeria.medtracker2.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galeria.medtracker2.feature.intakes.presentation.MainIntakesScreen
import com.galeria.medtracker2.feature.meds.presentation.AddMedicationScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.Home,
        ) {
            composable<AppRoutes.Home> {
                MainIntakesScreen(
                    onAddMedClick = {
                        navController.navigate(AppRoutes.AddMedication) { popUpTo(AppRoutes.Home) }
                    }
                )
            }

            composable<AppRoutes.Medications> {
                //
            }

            composable<AppRoutes.AddMedication> {
                AddMedicationScreen(
                    onMainClick = {
                        navController.navigate(AppRoutes.Home) { popUpTo(AppRoutes.AddMedication) }
                    }
                )
            }
        }
    }
}
