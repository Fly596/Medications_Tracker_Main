package com.galeria.medicationstracker.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.galeria.medicationstracker.ui.componentsOld.BottomNavigation
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.ResetPasswordScreen
import com.galeria.medicationstracker.ui.screens.auth.login.LoginScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DailyMedsScreen
import com.galeria.medicationstracker.ui.screens.dashboard.moodtracker.MoodTrackerScreen
import com.galeria.medicationstracker.ui.screens.medications.MedicationsScreen
import com.galeria.medicationstracker.ui.screens.medications.mediinfo.ViewMedicationInfoScreen
import com.galeria.medicationstracker.ui.screens.medications.newmed.NewMedicationDataScreen
import com.galeria.medicationstracker.ui.screens.profile.UserProfileScreen
import com.galeria.medicationstracker.ui.screens.profile.notes.NotesScreen
import com.galeria.medicationstracker.ui.screens.profile.profiledetails.ProfileDetailsScreen
import kotlinx.serialization.Serializable

@Composable
fun ApplicationNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel(),
) {
    val authState by viewModel.uiState.collectAsStateWithLifecycle()

    // Текущий элемент стека навигации.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Получаем route (уникальный идентификатор текущего экрана).
    val currentRoute =
        navBackStackEntry?.destination?.route
            ?: BottomNavigation.DASHBOARD.route::class.qualifiedName.orEmpty()
    // Очищаем route от query параметров (берём всё, что после "?").
    val currentRouteTrimmed by
    remember(currentRoute) { derivedStateOf { currentRoute.substringBefore("?") } } // was substringAfter.
    // Определяем, нужно ли показывать нижнюю панель навигации на текущем экране.
    val shouldShowBottomBar =
        BottomNavigation.entries.any { it.route::class.qualifiedName == currentRouteTrimmed }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomAppBar {
                    // Пробегаемся по всем пунктам нижнего меню.
                    BottomNavigation.entries.forEachIndexed { index, navigationItem ->
                        // Проверяем, выбран ли текущий пункт меню.
                        val isSelected by
                        remember(currentRoute) {
                            derivedStateOf {
                                currentRouteTrimmed == navigationItem.route::class.qualifiedName
                            }
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            label = { Text(navigationItem.title) },
                            icon = {
                                Icon(
                                    painterResource(navigationItem.selectedIcon),
                                    contentDescription = navigationItem.title,
                                )
                            },
                            onClick = { navController.navigate(navigationItem.route) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        val startDest = when (authState) {
            is AuthState.Authenticated -> GraphRoutes.Home
            else -> GraphRoutes.Auth
        }
        if (authState is AuthState.Loading) {
            //FullScreenLoader() // Не "Todo", а делай сразу нормально, ленивая ты задница
        }else{
            NavHost(
                navController = navController,
                startDestination = startDest,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                authGraph(navController)
                homeScreenGraph(navController)
                medicationsGraph(navController)
                profileGraph(navController)
            }
        }
        /*        when (authState) {
                    is AuthState.Loading -> {
                        // Todo: loading screen
                    }

                    is AuthState.Authenticated -> {
                        NavHost(
                            navController = navController,
                            startDestination = GraphRoutes.Home,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        ) {
                            authGraph(navController)
                            homeScreenGraph(navController)
                            medicationsGraph(navController)
                            profileGraph(navController)
                        }
                    }

                    is AuthState.Unauthenticated -> {
                        NavHost(
                            navController = navController,
                            startDestination = GraphRoutes.Auth,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        ) {
                            authGraph(navController)
                        }
                    }
                }*/
    }
}

@Serializable
sealed class GraphRoutes(val route: String) {
    
    @Serializable
    data object Auth : GraphRoutes("auth")
    
    @Serializable
    data object Home : GraphRoutes("home")
    
    @Serializable
    data object Medications : GraphRoutes("medications")
    
    @Serializable
    data object PatientDashboard : GraphRoutes("patient_dashboard")
}

// Граф для страниц аутификации.
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<GraphRoutes.Auth>(startDestination = AuthScreen.Login) {
        composable<AuthScreen.Login> {
            LoginScreen(
                onLoginSuccessNavigation = {
                    navController.navigate(GraphRoutes.Home /* HomeScreen.TodayMedications */) {
                        popUpTo(AuthScreen.Login) { inclusive = true }
                    }
                },
                onRegistration = { email ->
                    navController.navigate(AuthScreen.Registration(email))
                },
                onResetPassword = { email ->
                    navController.navigate(AuthScreen.PasswordRecovery(email))
                },
            )
        }

        composable<AuthScreen.Registration> { navBackStackEntry ->
            SignupScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable<AuthScreen.PasswordRecovery> { navBackStackEntry ->
            ResetPasswordScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}

fun NavGraphBuilder.homeScreenGraph(navController: NavHostController) {
    navigation<GraphRoutes.Home>(startDestination = HomeScreen.TodayMedications) {
        composable<HomeScreen.TodayMedications> {
            DailyMedsScreen(
                onAddMood = { navController.navigate(HomeScreen.MoodCheck) },
                onAddIntake = {},
            )
        }

        composable<HomeScreen.MoodCheck> {
            MoodTrackerScreen(onBackClick = { navController.navigateUp() })
            // TODO: MoodTrackerScreen
        }
    }
}

fun NavGraphBuilder.medicationsGraph(navController: NavHostController) {
    navigation<GraphRoutes.Medications>(startDestination = MedicationScreen.MedicationsList) {
        composable<MedicationScreen.MedicationsList> {
            MedicationsScreen(
                onAddClick = { navController.navigate(MedicationScreen.AddMedication) },
                onViewClick = { id ->
                    navController.navigate(
                        MedicationScreen.ViewMedication(
                            id
                        )
                    )
                },
                onEditClick = { id ->
                    navController.navigate(MedicationScreen.UpdateMedication(id))
                },
            )
            // TODO: MedicationsListScreen
        }
        composable<MedicationScreen.AddMedication> {
            NewMedicationDataScreen(navigateBack = { navController.navigateUp() })
        }
        composable<MedicationScreen.ViewMedication> { navBackStackEntry ->
            val viewMedication: MedicationScreen.ViewMedication =
                navBackStackEntry.toRoute()
            ViewMedicationInfoScreen(onNavigateToMedsList = { navController.navigateUp() })
            // TODO: ViewMedicationScreen(medicationId =
        }
        composable<MedicationScreen.UpdateMedication> {
            // TODO: UpdateMedicationScreen
        }
    }
}

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigation<GraphRoutes.PatientDashboard>(startDestination = ProfileScreen.ProfileMain) {
        composable<ProfileScreen.ProfileMain> {
            UserProfileScreen(
                onEditProfileClick = { navController.navigate(ProfileScreen.ProfileOverview) },
                onNotesClick = { navController.navigate(ProfileScreen.Notes) },
            )
        }
        
        composable<ProfileScreen.ProfileOverview> { ProfileDetailsScreen() }
        
        composable<ProfileScreen.Notes> { NotesScreen() }
    }
}
