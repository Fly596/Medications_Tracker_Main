package com.galeria.medicationstracker.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.galeria.medicationstracker.ui.screens.admin.AddMedicationScreen
import com.galeria.medicationstracker.ui.screens.admin.AddMedicationsViewModel
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.AccountRecoveryScreen
import com.galeria.medicationstracker.ui.screens.auth.login.LoginScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DashboardScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DashboardVM
import com.galeria.medicationstracker.ui.screens.dashboard.moodtracker.MoodTrackerScreen
import com.galeria.medicationstracker.ui.screens.dashboard.moodtracker.MoodTrackerVM
import com.galeria.medicationstracker.ui.screens.medications.MedicationsScreen
import com.galeria.medicationstracker.ui.screens.medications.MedicationsViewModel
import com.galeria.medicationstracker.ui.screens.medications.MedsPagesViewModel
import com.galeria.medicationstracker.ui.screens.medications.mediinfo.ViewMedicationInfoScreen
import com.galeria.medicationstracker.ui.screens.medications.newmed.AddNewMedViewModel
import com.galeria.medicationstracker.ui.screens.medications.newmed.NewMedicationDataScreen
import com.galeria.medicationstracker.ui.screens.medications.update.UpdateMedScreen
import com.galeria.medicationstracker.ui.screens.medications.update.UpdateMedVM
import com.galeria.medicationstracker.ui.screens.profile.AccountScreenHead
import com.galeria.medicationstracker.ui.screens.profile.ProfileVM
import com.galeria.medicationstracker.ui.screens.profile.notes.NewNoteScreen
import com.galeria.medicationstracker.ui.screens.profile.notes.NewNoteViewModel
import com.galeria.medicationstracker.ui.screens.profile.notes.NotesScreen
import com.galeria.medicationstracker.ui.screens.profile.notes.NotesScreenViewModel
import com.galeria.medicationstracker.ui.screens.profile.profiledetails.ProfileDetailsScreen
import com.galeria.medicationstracker.ui.screens.profile.profiledetails.ProfileDetailsViewModel
import com.galeria.medicationstracker.utils.navigation.Routes.AuthRoutes
import com.galeria.medicationstracker.utils.navigation.Routes.PatientRoutes
import kotlinx.serialization.Serializable

@Composable
fun ApplicationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.NavigationRoutes.AUTH,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,/* HH */
        modifier = modifier,
    ) {
        authGraph(navController)
        patientGraph(navController)
        // userMedsGraph(navController)
    }
}

@Serializable
sealed class Routes {

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
        @Serializable
        data object Auth : AuthRoutes()

        @Serializable
        data object Login : AuthRoutes()

        @Serializable
        data object Registration : AuthRoutes()

        @Serializable
        data object PasswordRecovery : AuthRoutes()
    }

    // TODO: extract to separate file.
    @Serializable
    sealed class PatientRoutes {

        @Serializable
        object Patient : PatientRoutes()

        // home screens.
        @Serializable
        object PatientHome : PatientRoutes()

        @Serializable
        data object PatientTodayMedications : PatientRoutes()

        @Serializable
        data object PatientLogs : PatientRoutes()

        // medications screens.
        @Serializable
        object PatientMedications : PatientRoutes()

        @Serializable
        data object PatientListMedications : PatientRoutes()

        @Serializable
        data object PatientAddMedication : PatientRoutes()

        @Serializable
        data object AdminAddMedication : PatientRoutes()

        @Serializable
        data object PatientViewMedication : PatientRoutes()

        @Serializable
        data class PatientUpdateMedication(val medicationName: String?) : PatientRoutes()

        // profile screen.
        @Serializable
        object PatientInfo : PatientRoutes()

        @Serializable
        object MoodCheck : PatientRoutes()

        @Serializable
        data object PatientProfile : PatientRoutes()

        @Serializable
        data object PatientProfileOverview : PatientRoutes()

        @Serializable
        data object PatientNotes : PatientRoutes()

        @Serializable
        data object PatientNewNote : PatientRoutes()

        @Serializable
        data object PatientWeightDialog : PatientRoutes() // dialog.

        @Serializable
        data object PatientHeightDialog : PatientRoutes() // dialog.

        @Serializable
        data object PatientSettings : PatientRoutes() // dialog.

        @Serializable
        data object PatientAppointment : PatientRoutes() // dialog.
    }

    @Serializable
    sealed class DoctorRoutes {

        @Serializable
        object Doctor : DoctorRoutes()

        // home screens. Расписание на день.
        @Serializable
        object DocHome : DoctorRoutes()

        @Serializable
        data object DocDashboard : DoctorRoutes()

        @Serializable
        object DocPatients : DoctorRoutes()

        @Serializable
        data object DocPatientsList : DoctorRoutes()

        @Serializable
        data object DocPatientInfo : DoctorRoutes()
    }

    @Serializable
    sealed class AdminRoutes {

        @Serializable
        data object AdminDashboard : AdminRoutes()
    }
}

// Граф для страниц аутификации.
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthRoutes.Auth>(startDestination = AuthRoutes.Login) {
        composable<AuthRoutes.Login> {
            LoginScreen(

                onLogin = {
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
            val vm: DashboardVM = hiltViewModel()
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
                dashboardViewModel = vm,
            )
        }
        composable<PatientRoutes.MoodCheck> {
            val vm: MoodTrackerVM = hiltViewModel()
            MoodTrackerScreen(onBackClick = { navController.popBackStack() }, viewModel = vm)
        }
    }
}

// Граф для страницы с лекарствами.
fun NavGraphBuilder.patientMedsGraph(navController: NavHostController) {
    navigation<PatientRoutes.PatientMedications>(
        startDestination = PatientRoutes.PatientListMedications
    ) {
        composable<PatientRoutes.PatientListMedications> {
            val medsPagesVM: MedsPagesViewModel = hiltViewModel()
            val vm: MedicationsViewModel = hiltViewModel()
            MedicationsScreen(
                medsPagesVM = medsPagesVM,
                onAddMedClick = {
                    // Добавление лекарства.
                    navController.navigate(PatientRoutes.PatientAddMedication)
                },
                onViewMed = {
                    // Просмотр лекарства.
                    navController.navigate(PatientRoutes.PatientViewMedication) {
                        popUpTo(PatientRoutes.PatientListMedications) { inclusive = true }
                    }
                },
                onEditMedClick = { name ->
                    // Редактирование лекарства.
                    navController.navigate(PatientRoutes.PatientUpdateMedication(name))
                },
                medicationsViewModel = vm,
                onAddAdminMedClick = {
                    navController.navigate(PatientRoutes.AdminAddMedication)
                }
            )
        }

        composable<PatientRoutes.PatientAddMedication> {
            val vm: AddNewMedViewModel = hiltViewModel()
            NewMedicationDataScreen(
                onConfirmClick = { navController.popBackStack() },
                viewModel = vm,
            )
        }

        composable<PatientRoutes.AdminAddMedication> {
            val vm: AddMedicationsViewModel = hiltViewModel()
            AddMedicationScreen(
                modifier = Modifier,
                onConfirmClick = { navController.popBackStack() },
                onBackClick = { navController.navigateUp() },
                viewModel = vm,
            )
        }

        composable<PatientRoutes.PatientViewMedication> {
            val medsPagesVM: MedsPagesViewModel = hiltViewModel()

            ViewMedicationInfoScreen(
                medsViewModel = medsPagesVM,
                onReturn = { navController.navigateUp() },
            )
        }

        composable<PatientRoutes.PatientUpdateMedication> { backStackEntry ->
            val vm: UpdateMedVM = hiltViewModel()
            val args = backStackEntry.toRoute<PatientRoutes.PatientUpdateMedication>()

            UpdateMedScreen(
                passedMedName = args.medicationName ?: "",
                viewModel = vm,
                onBack = { navController.navigateUp() },
            )
        }
    }
}

fun NavGraphBuilder.patientProfileGraph(navController: NavHostController) {
    navigation<PatientRoutes.PatientInfo>(startDestination = PatientRoutes.PatientProfile) {
        composable<PatientRoutes.PatientProfile> {
            val accVm: ProfileVM = hiltViewModel()

            AccountScreenHead(
                onNotesClick = { navController.navigate(PatientRoutes.PatientNotes) },
                onProfileClick = { navController.navigate(PatientRoutes.PatientProfileOverview) },
                viewModel = accVm,
            )
        }

        composable<PatientRoutes.PatientNotes> {
            val notesVm: NotesScreenViewModel = hiltViewModel()
            NotesScreen(
                onBackClick = { navController.popBackStack() },
                onNewNoteClick = { navController.navigate(PatientRoutes.PatientNewNote) },
                viewModel = notesVm,
            )
        }

        composable<PatientRoutes.PatientNewNote> {
            val newNoteVm: NewNoteViewModel = hiltViewModel()
            NewNoteScreen(onBackClick = { navController.popBackStack() }, viewModel = newNoteVm)
        }

        composable<PatientRoutes.PatientProfileOverview> {
            val vm: ProfileDetailsViewModel = hiltViewModel()
            ProfileDetailsScreen(onBackClick = { navController.navigateUp() }, viewModel = vm)
        }
    }
}
