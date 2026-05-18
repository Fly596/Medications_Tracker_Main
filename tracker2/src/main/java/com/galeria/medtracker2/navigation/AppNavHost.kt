package com.galeria.medtracker2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.galeria.medtracker2.feature.tracker.presentation.add_med.AddMedicationScreen
import com.galeria.medtracker2.feature.tracker.presentation.medications.MyMedicationsScreen
import com.galeria.medtracker2.feature.tracker.presentation.schedule.MainIntakesScreen
import java.util.UUID

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = AppRoutes.AddMedicationRoute,
    ) {
        composable<AppRoutes.Home> {
            MainIntakesScreen(
                onNavigateToAddMedication = {
                    navController.navigate(AppRoutes.AddMedicationRoute) { popUpTo(AppRoutes.Home) }
                },
                onNavigateToMedicationsList = {
                    navController.navigate(AppRoutes.MedicationsListRoute) {
                        popUpTo(AppRoutes.Home)
                    }
                },
            )
        }

        composable<AppRoutes.AddMedicationRoute> {
            AddMedicationScreen(
                onMainClick = {
                    navController.navigate(AppRoutes.Home) { popUpTo(AppRoutes.AddMedicationRoute) }
                }
            )
        }

        composable<AppRoutes.MedicationsListRoute> {
            MyMedicationsScreen(
                onNavigateToViewMedication = { id ->
                    navController.navigate(AppRoutes.MedicationDetailsRoute(id.toString()))
                },
                onNavigateToAddMedication = { navController.navigate(AppRoutes.AddMedicationRoute) },
            )
        }

        composable<AppRoutes.MedicationDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoutes.MedicationDetailsRoute>()
            val medicationId = UUID.fromString(route.medicationId)

            // MyMedicationsScreen(onNavigateToViewMedication = { id -> navController.navigate() })
        }
    }
}
