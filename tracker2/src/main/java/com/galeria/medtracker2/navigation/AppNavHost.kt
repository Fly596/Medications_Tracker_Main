package com.galeria.medtracker2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.feature.tracker.presentation.add_med.AddMedicationScreen
import com.galeria.medtracker2.feature.tracker.presentation.medications.MyMedicationsScreen
import com.galeria.medtracker2.feature.tracker.presentation.schedule.MainIntakesScreen
import java.util.UUID

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                tonalElevation = 12.dp,
                containerColor = MedTrackerTheme.colors.secondaryBackground
            ) {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<AppRoutes.Home>() } == true,
                    onClick = {
                        navController.navigate(AppRoutes.Home) {
                            popUpTo(0) { saveState = true }; launchSingleTop = true;
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                // Второй таб.
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<AppRoutes.MedicationsListRoute>() } == true,
                    onClick = {
                        navController.navigate(AppRoutes.MedicationsListRoute) {
                            popUpTo(0) { saveState = true }; launchSingleTop = true;
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "My Medications") },
                    label = { Text("Medications") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.Home,
        ) {
            composable<AppRoutes.Home> {
                MainIntakesScreen()
            }

            composable<AppRoutes.AddMedicationRoute> {
                AddMedicationScreen(
                    onConfirm = {
                        navController.navigate(AppRoutes.Home)
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
}
