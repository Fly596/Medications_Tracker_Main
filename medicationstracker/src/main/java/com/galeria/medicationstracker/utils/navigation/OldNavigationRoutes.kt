package com.galeria.medicationstracker.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.AccountRecoveryScreen
import com.galeria.medicationstracker.ui.screens.auth.login.LoginScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DashboardScreen
import com.galeria.medicationstracker.ui.screens.dashboard.moodtracker.MoodTrackerScreen
import com.galeria.medicationstracker.ui.screens.medications.MedicationsScreen
import com.galeria.medicationstracker.ui.screens.medications.mediinfo.ViewMedicationInfoScreen
import com.galeria.medicationstracker.ui.screens.medications.newmed.NewMedicationDataScreen
import com.galeria.medicationstracker.ui.screens.medications.update.UpdateMedScreen
import com.galeria.medicationstracker.ui.screens.profile.AccountScreenHead
import com.galeria.medicationstracker.ui.screens.profile.notes.NewNoteScreen
import com.galeria.medicationstracker.ui.screens.profile.notes.NotesScreen
import com.galeria.medicationstracker.ui.screens.profile.profiledetails.ProfileDetailsScreen
import com.galeria.medicationstracker.utils.navigation.RoutesOld.AuthRoutes
import com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes
import kotlinx.serialization.Serializable

@Composable
fun ApplicationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RoutesOld.NavigationRoutes.AUTH,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination, /* HH */
        modifier = modifier,
    ) {
        authGraph(navController)
        patientGraph(navController)
        // userMedsGraph(navController)
    }
}

@Serializable
sealed class RoutesOld {

    object NavigationRoutes {

        const val AUTH = "com.galeria.medicationstracker.utils.navigation.Routes.AuthRoutes.Auth"
        const val LOGIN = "com.galeria.medicationstracker.utils.navigation.Routes.AuthRoutes.Login"
        const val REGISTRATION =
            "com.galeria.medicationstracker.utils.navigation.Routes.AuthRoutes.Registration"
        const val PASSWORD_RECOVERY =
            "com.galeria.medicationstracker.utils.navigation.Routes.AuthRoutes.PasswordRecovery"
        const val PATIENT_DASHBOARD =
            "com.galeria.medicationstracker.utils.navigation.Routes.PatientRoutes.Patient"
        const val PATIENT_MEDICATIONS =
            "com.galeria.medicationstracker.utils.navigation.Routes.PatientRoutes.PatientListMedications"
        const val PATIENT_NEW_MEDICATION =
            "com.galeria.medicationstracker.utils.navigation.Routes.PatientRoutes.PatientAddMedication"
        const val PATIENT_PROFILE =
            "com.galeria.medicationstracker.utils.navigation.Routes.PatientRoutes.PatientProfile"
        const val PATIENT_SETTINGS =
            "com.galeria.medicationstracker.utils.navigation.Routes.PatientRoutes.PatientProfile"
        const val DOC_DASHBOARD =
            "com.galeria.medicationstracker.utils.navigation.Routes.DoctorRoutes.Doctor"
        const val DOC_PATIENTS_LIST =
            "com.galeria.medicationstracker.utils.navigation.Routes.DoctorRoutes.DocPatientsList"
        const val ADMIN_DASHBOARD =
            "com.galeria.medicationstracker.utils.navigation.Routes.DoctorRoutes.AdminDashboard"
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
        data class PatientUpdateMedication(val medicationName: String?) : PatientRoutes()

        // profile screen.
        @Serializable object PatientInfo : PatientRoutes()

        @Serializable object MoodCheck : PatientRoutes()

        @Serializable data object PatientProfile : PatientRoutes()

        @Serializable data object PatientProfileOverview : PatientRoutes()

        @Serializable data object PatientNotes : PatientRoutes()

        @Serializable data object PatientNewNote : PatientRoutes()

        @Serializable
        data object PatientWeightDialog : PatientRoutes() // dialog.

        @Serializable
        data object PatientHeightDialog : PatientRoutes() // dialog.

        @Serializable
        data object PatientSettings : PatientRoutes() // dialog.

        @Serializable
        data object PatientAppointment : PatientRoutes() // dialog.
    }
}

// Граф для страниц аутификации.
fun NavGraphBuilder.authGraph(navController: NavHostController) {
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
            AccountRecoveryScreen(navigateHome = { navController.navigateUp() })
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
            DashboardScreen(
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

        composable<PatientRoutes.PatientViewMedication> { backStackEntry ->
            val args = backStackEntry.toRoute<PatientRoutes.PatientViewMedication>()

            ViewMedicationInfoScreen(onReturn = { navController.navigateUp() })
        }

        composable<PatientRoutes.PatientUpdateMedication> { backStackEntry ->
            val args = backStackEntry.toRoute<PatientRoutes.PatientUpdateMedication>()

            UpdateMedScreen(
                passedMedName = args.medicationName ?: "",
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
