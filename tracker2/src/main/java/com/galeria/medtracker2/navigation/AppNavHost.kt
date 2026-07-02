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
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.feature.tracker.presentation.add_med.AddMedicationScreen
import com.galeria.medtracker2.feature.tracker.presentation.medication.MedicationScreen
import com.galeria.medtracker2.feature.tracker.presentation.medications.MyMedicationsScreen
import com.galeria.medtracker2.feature.tracker.presentation.schedule.MainIntakesScreen

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
                containerColor = MedTrackerTheme.colors.secondaryBackground,
            ) {
                NavigationBarItem(
                    selected =
                        currentDestination?.hierarchy?.any { it.hasRoute<AppRoutes.Home>() } ==
                            true,
                    onClick = {
                        navController.navigate(AppRoutes.Home) {
                            popUpTo(0) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                )

                // Второй таб.
                NavigationBarItem(
                    selected =
                        currentDestination?.hierarchy?.any {
                            it.hasRoute<AppRoutes.MedicationsList>()
                        } == true,
                    onClick = {
                        navController.navigate(AppRoutes.MedicationsList) {
                            popUpTo(0) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "My Medications") },
                    label = { Text("Medications") },
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.Home,
        ) {
            composable<AppRoutes.Home> { MainIntakesScreen() }

            composable<AppRoutes.AddMedication> {
                AddMedicationScreen(onConfirm = { navController.navigate(AppRoutes.Home) })
            }

            composable<AppRoutes.MedicationsList> {
                MyMedicationsScreen(
                    onNavigateToViewMedication = { id ->
                        navController.navigate(AppRoutes.MedicationDetails(id.toString()))
                    },
                    onNavigateToAddMedication = {
                        navController.navigate(AppRoutes.AddMedication)
                    },
                )
            }

            composable<AppRoutes.MedicationDetails> { backStackEntry ->
                // val route = backStackEntry.toRoute<AppRoutes.MedicationDetails>()
                // val medicationId = UUID.fromString(route.medicationId)

                MedicationScreen(onNavigateBack = { navController.navigateUp() })
            }
        }
    }
}
