package com.galeria.medtracker2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.galeria.medtracker2.feature.intakes.presentation.MainIntakesScreen
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import com.galeria.medtracker2.feature.meds.presentation.AddMedicationScreen
import com.galeria.medtracker2.feature.meds.presentation.MyMedicationsScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    // Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = AppRoutes.AddMedication,
    ) {
        composable<AppRoutes.Home> {
            MainIntakesScreen(
                onNavigateToAddMedication = {
                    navController.navigate(AppRoutes.AddMedication) { popUpTo(AppRoutes.Home) }
                }
            )
        }

        composable<AppRoutes.AddMedication> {
            AddMedicationScreen(
                onMainClick = {
                    navController.navigate(AppRoutes.Home) { popUpTo(AppRoutes.AddMedication) }
                }
            )
        }

        composable<AppRoutes.MedicationsList> {
            MyMedicationsScreen(
                onNavigateToViewMedication = { id ->
                    navController.navigate(AppRoutes.Medication(id))
                }
            )
        }

        composable<AppRoutes.Medication> { backStackEntry ->
            val medication: MedicationEntity = backStackEntry.toRoute()

            // MyMedicationsScreen(onNavigateToViewMedication = { id -> navController.navigate() })
        }
    }
    // }
}
