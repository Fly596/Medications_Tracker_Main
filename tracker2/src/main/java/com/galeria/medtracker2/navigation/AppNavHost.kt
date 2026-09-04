package com.galeria.medtracker2.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.feature.medication.presentation.add_med.AddMedScreen
import com.galeria.medtracker2.feature.medication.presentation.meds_list.MyMedsScreen
import com.galeria.medtracker2.feature.medication.presentation.view_med.ViewMedScreen

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
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.home_1),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    label = { Text("Home") },
                )
                // Второй таб.
                NavigationBarItem(
                    selected =
                            currentDestination?.hierarchy?.any {
                                it.hasRoute<AppRoutes.ProfileOverview>()
                            } == true,
                    onClick = {
                        navController.navigate(AppRoutes.ProfileOverview) {
                            popUpTo(0) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.list),
                            contentDescription = "My Medications",
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    label = { Text("Overview") },
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.Home,
        ) {
            composable<AppRoutes.Home> {
                MyMedsScreen(
                    onNavigateToViewMedication = { id ->
                        navController.navigate(AppRoutes.MedicationDetails(id.toString()))
                    },
                    onNavigateToAddMedication = {
                        navController.navigate(AppRoutes.AddMedication)
                    },
                    onAddIntake = {
                        navController.navigate(AppRoutes.AddIntake)
                    },
                )
            }


            composable<AppRoutes.AddMedication> {
                AddMedScreen(
                    onConfirm = { navController.navigate(AppRoutes.Home) },
                    onBack = { navController.navigateUp() })
            }

            composable<AppRoutes.MedicationsList> {
                MyMedsScreen(
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
                ViewMedScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onEditMedication = { id ->
                        navController.navigate(AppRoutes.EditMedication(id.toString()))
                    },
                    onAddIntake = { id ->
                        navController.navigate(AppRoutes.AddIntake(id.toString()))
                    }
                )
            }

            composable<AppRoutes.EditMedication> { backStackEntry ->
                //val route = backStackEntry.toRoute<AppRoutes.EditMedication>()
                //val medicationId = UUID.fromString(route.medicationId)
                //EditMedicationScreen(onNavigateBack = { navController.navigateUp() })
            }

            composable<AppRoutes.ProfileOverview> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Work in progress",
                        style = MedTrackerTheme.typography.display3Emphasized
                    )
                }
                //ProfileOverviewScreen()
            }

            composable<AppRoutes.AddIntake> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Work in progress",
                        style = MedTrackerTheme.typography.display3Emphasized
                    )
                }
                //AddIntakeScreen()
            }
        }
    }
}
