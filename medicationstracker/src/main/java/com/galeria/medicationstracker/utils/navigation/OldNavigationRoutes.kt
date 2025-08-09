package com.galeria.medicationstracker.utils.navigation

import kotlinx.serialization.Serializable

/* @Composable
fun ApplicationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RoutesOld.NavigationRoutes.AUTH,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,  *//* HH *//*
        modifier = modifier,
    ) {
        // authGraphOld(navController)
        // patientGraph(navController)
        // userMedsGraph(navController)
    }
} */

@Serializable
sealed class RoutesOld {

    object NavigationRoutes {
        
        const val AUTH =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.AuthRoutes.Auth"
        const val LOGIN =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.AuthRoutes.Login"
        const val REGISTRATION =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.AuthRoutes.Registration"
        const val PASSWORD_RECOVERY =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.AuthRoutes.PasswordRecovery"
        const val PATIENT_DASHBOARD =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes.Patient"
        const val PATIENT_MEDICATIONS =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes.PatientListMedications"
        const val PATIENT_NEW_MEDICATION =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes.PatientAddMedication"
        const val PATIENT_PROFILE =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes.PatientProfile"
        const val PATIENT_SETTINGS =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes.PatientProfile"
        const val DOC_DASHBOARD =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.DoctorRoutes.Doctor"
        const val DOC_PATIENTS_LIST =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.DoctorRoutes.DocPatientsList"
        const val ADMIN_DASHBOARD =
            "com.galeria.medicationstracker.utils.navigation.RoutesOld.DoctorRoutes.AdminDashboard"
        // ... other routes
    }

    @Serializable
    sealed class AuthRoutes {

        // Authentification pages.
        @Serializable data object Auth : AuthRoutes()

        @Serializable data object Login : AuthRoutes()

        @Serializable data object Registration : AuthRoutes()

        @Serializable data object PasswordRecovery : AuthRoutes()
    }

    // TODO: extract to separate file.
    @Serializable
    sealed class PatientRoutes {

        @Serializable object Patient : PatientRoutes()

        // home screens.
        @Serializable object PatientHome : PatientRoutes()

        @Serializable data object PatientTodayMedications : PatientRoutes()

        @Serializable data object PatientLogs : PatientRoutes()

        // medications screens.
        @Serializable object PatientMedications : PatientRoutes()

        @Serializable data object PatientListMedications : PatientRoutes()

        @Serializable data object PatientAddMedication : PatientRoutes()

        @Serializable data object AdminAddMedication : PatientRoutes()

        @Serializable data class PatientViewMedication(val medicationId: String) : PatientRoutes()

        @Serializable
        data class PatientUpdateMedication(val medicationId: String?) :
            PatientRoutes()

        // profile screen.
        @Serializable object PatientInfo : PatientRoutes()

        @Serializable object MoodCheck : PatientRoutes()

        @Serializable data object PatientProfile : PatientRoutes()

        @Serializable data object PatientProfileOverview : PatientRoutes()

        @Serializable data object PatientNotes : PatientRoutes()

        @Serializable data object PatientNewNote : PatientRoutes()
    }
}
/*

// Граф для страниц аутификации.
fun NavGraphBuilder.authGraphOld(navController: NavHostController) {
    navigation<AuthRoutes.Auth>(startDestination = AuthRoutes.Login) {
        composable<AuthRoutes.Login> {
            LoginScreen(
                onLoginSuccessNavigation = {
                    navController.navigate(PatientRoutes.Patient) {
                        popUpTo(AuthRoutes.Login) { inclusive = true }
                    }
                },
                onRegistration = { navController.navigate(AuthRoutes.Registration) },
                onResetPassword = { navController.navigate(AuthRoutes.PasswordRecovery) },
            )
        }

        composable<AuthRoutes.Registration> {
            SignupScreen(navigateHome = { navController.navigateUp() })
        }

        composable<AuthRoutes.PasswordRecovery> {
            ResetPasswordScreen(navigateHome = { navController.navigateUp() })
        }
    }
}

// Граф для страниц приложения.
fun NavGraphBuilder.patientGraph(navController: NavHostController) {
    navigation<PatientRoutes.Patient>(startDestination = PatientRoutes.PatientHome) {
        patientDashboardGraph(navController)
        // страница с лекарствами.
        patientMedsGraph(navController)

        patientProfileGraph(navController)
    }
}

fun NavGraphBuilder.patientDashboardGraph(navController: NavHostController) {
    navigation<PatientRoutes.PatientHome>(
        startDestination = PatientRoutes.PatientTodayMedications
    ) {
        composable<PatientRoutes.PatientTodayMedications> {
            DailyMedsScreen(
                onAddMood = {
                    // open logs history screen.
                    navController.navigate(PatientRoutes.MoodCheck) {
                        popUpTo(PatientRoutes.PatientTodayMedications) {}
                    }
                },
                onAddMedClick = {
                    // open medications screen.
                    navController.navigate(PatientRoutes.PatientMedications) {
                        popUpTo(PatientRoutes.PatientTodayMedications) {}
                    }
                },
            )
        }
        composable<PatientRoutes.MoodCheck> {
            MoodTrackerScreen(onBackClick = { navController.popBackStack() })
        }
    }
}

// Граф для страницы с лекарствами.
fun NavGraphBuilder.patientMedsGraph(navController: NavHostController) {
    navigation<PatientRoutes.PatientMedications>(
        startDestination = PatientRoutes.PatientListMedications
    ) {
        composable<PatientRoutes.PatientListMedications> {
            MedicationsScreen(
                onAddClick = {
                    // Добавление лекарства.
                    navController.navigate(PatientRoutes.PatientAddMedication)
                },
                onViewClick = { medicatioID ->
                    // Просмотр лекарства.
                    navController.navigate(PatientRoutes.PatientViewMedication(medicatioID)) {
                        popUpTo(PatientRoutes.PatientListMedications) { inclusive = true }
                    }
                },
                onEditClick = { medicatioID ->
                    // Редактирование лекарства.
                    navController.navigate(PatientRoutes.PatientUpdateMedication(medicatioID))
                },
            )
        }

        composable<PatientRoutes.PatientAddMedication> {
            NewMedicationDataScreen(onConfirmClick = { navController.popBackStack() })
        }

        composable<PatientRoutes.PatientViewMedication> {
            ViewMedicationInfoScreen(onNavigateToMedsList = { navController.navigateUp() })
        }

        composable<PatientRoutes.PatientUpdateMedication> { backStackEntry ->
            val args = backStackEntry.toRoute<PatientRoutes.PatientUpdateMedication>()
            UpdateMedScreen(
                passedMedId = args.medicationId ?: "",
                onBack = { navController.navigateUp() },
            )
        }
    }
}

fun NavGraphBuilder.patientProfileGraph(navController: NavHostController) {
    navigation<PatientRoutes.PatientInfo>(startDestination = PatientRoutes.PatientProfile) {
        composable<PatientRoutes.PatientProfile> {
            AccountScreenHead(
                onNotesClick = { navController.navigate(PatientRoutes.PatientNotes) },
                onProfileClick = { navController.navigate(PatientRoutes.PatientProfileOverview) },
            )
        }

        composable<PatientRoutes.PatientNotes> {
            NotesScreen(
                onBackClick = { navController.popBackStack() },
                onNewNoteClick = { navController.navigate(PatientRoutes.PatientNewNote) },
            )
        }

        composable<PatientRoutes.PatientNewNote> {
            NewNoteScreen(onBackClick = { navController.popBackStack() })
        }

        composable<PatientRoutes.PatientProfileOverview> {
            ProfileDetailsScreen(onBackClick = { navController.navigateUp() })
        }
    }
}
*/
