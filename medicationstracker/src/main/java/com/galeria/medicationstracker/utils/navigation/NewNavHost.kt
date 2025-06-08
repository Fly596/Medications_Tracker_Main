package com.galeria.medicationstracker.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.ResetPasswordScreen
import com.galeria.medicationstracker.ui.screens.auth.login.LoginScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.CheckIntakeScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DashboardScreenNew
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
        dashboardGraph(navController)
        // medicationsGraph(navController)
        // profileGraph(navController)
        // userMedsGraph(navController)
    }
}

@Serializable
sealed class Routes(val route: String) {
    
    @Serializable
    data object Auth : Routes("auth")
    
    @Serializable
    data object Home : Routes("home")
    
    @Serializable
    data object Medications : Routes("medications")
    
    @Serializable
    data object PatientDashboard : Routes("patient_dashboard")
}

// Граф для страниц аутификации.
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Routes.Auth>(startDestination = AuthScreen.Login) {
        composable<AuthScreen.Login> {
            LoginScreen(
                onLoginSuccessNavigation = {
                    navController.navigate(HomeScreen.TodayMedications) {
                        popUpTo(AuthScreen.Login) { inclusive = true }
                    }
                },
                onRegistration = { navController.navigate(AuthScreen.Registration) },
                onResetPassword = { navController.navigate(AuthScreen.PasswordRecovery) },
            )
        }

        composable<AuthScreen.Registration> {
            SignupScreen(navigateHome = { navController.navigateUp() })
        }

        composable<AuthScreen.PasswordRecovery> {
            ResetPasswordScreen(navigateHome = { navController.navigateUp() })
        }
    }
}

fun NavGraphBuilder.dashboardGraph(navController: NavHostController) {
    navigation<Routes.Home>(startDestination = HomeScreen.TodayMedications) {
        composable<HomeScreen.TodayMedications> {
            // TODO
            DashboardScreenNew(
                onAddMood = { navController.navigate(HomeScreen.MoodCheck) },
                onAddIntake = { navController.navigate(HomeScreen.IntakeCheck(it)) },
            )
        }
        
        composable<HomeScreen.IntakeCheck> { navBackStackEntry ->
            val intakeDialog: HomeScreen.IntakeCheck =
                navBackStackEntry.toRoute()
            CheckIntakeScreen(
                intakeCheck = intakeDialog,
                onNavigateBack = { navController.navigateUp() },
            )
        }
        
        composable<HomeScreen.MoodCheck> {}
    }
}
