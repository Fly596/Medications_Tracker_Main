package com.galeria.medicationstracker.model.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.model.navigation.Routes.AdminRoutes
import com.galeria.medicationstracker.model.navigation.Routes.AuthRoutes
import com.galeria.medicationstracker.model.navigation.Routes.DoctorRoutes
import com.galeria.medicationstracker.model.navigation.Routes.PatientRoutes
import com.galeria.medicationstracker.ui.doctor.home.DocDashboardScreen
import com.galeria.medicationstracker.ui.doctor.patients.PatientsListScreen
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.AccountRecoveryScreen
import com.galeria.medicationstracker.ui.screens.auth.login.LoginScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DashboardScreen
import com.galeria.medicationstracker.ui.screens.dashboard.record.IntakeRecordsScreen
import com.galeria.medicationstracker.ui.screens.medications.MedicationsScreen
import com.galeria.medicationstracker.ui.screens.medications.MedicationsViewModel
import com.galeria.medicationstracker.ui.screens.medications.MedsPagesViewModel
import com.galeria.medicationstracker.ui.screens.medications.NewMedicationDataScreen
import com.galeria.medicationstracker.ui.screens.medications.mediinfo.ViewMedicationInfoScreen
import com.galeria.medicationstracker.ui.screens.medications.update.UpdateMedScreen
import com.galeria.medicationstracker.ui.screens.profile.ProfileScreen
import com.galeria.medicationstracker.ui.screens.profile.ProfileVM
import com.galeria.medicationstracker.ui.screens.profile.appoinment.AppointmentScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    object NavigationRoutes {

        const val LOGIN =
            "com.galeria.medicationstracker.model.navigation.Routes.AuthRoutes.Login"
        const val REGISTRATION =
            "com.galeria.medicationstracker.model.navigation.Routes.AuthRoutes.Registration"
        const val PASSWORD_RECOVERY =
            "com.galeria.medicationstracker.model.navigation.Routes.AuthRoutes.PasswordRecovery"

        const val PATIENT_DASHBOARD =
            "com.galeria.medicationstracker.model.navigation.Routes.PatientRoutes.PatientTodayMedications"
        const val PATIENT_MEDICATIONS =
            "com.galeria.medicationstracker.model.navigation.Routes.PatientRoutes.PatientListMedications"
        const val PATIENT_NEW_MEDICATION =
            "com.galeria.medicationstracker.model.navigation.Routes.PatientRoutes.PatientAddMedication"
        const val PATIENT_PROFILE =
            "com.galeria.medicationstracker.model.navigation.Routes.PatientRoutes.PatientProfile"
        const val PATIENT_SETTINGS =
            "com.galeria.medicationstracker.model.navigation.Routes.PatientRoutes.PatientProfile"

        const val DOC_DASHBOARD =
            "com.galeria.medicationstracker.model.navigation.Routes.DoctorRoutes.DocDashboard"
        const val DOC_PATIENTS_LIST =
            "com.galeria.medicationstracker.model.navigation.Routes.DoctorRoutes.DocPatientsList"
        const val ADMIN_DASHBOARD =
            "com.galeria.medicationstracker.model.navigation.Routes.DoctorRoutes.AdminDashboard"
        // ... other routes
    }

    @Serializable
    sealed class AuthRoutes {

        // Authentification pages.
        @Serializable object Auth : AuthRoutes()
        @Serializable data object Login : AuthRoutes()
        @Serializable data object Registration : AuthRoutes()
        @Serializable data object PasswordRecovery : AuthRoutes()
    }

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
        @Serializable data object PatientViewMedication : PatientRoutes()
        @Serializable data class PatientUpdateMedication(val medicationName: String?) :
            PatientRoutes()

        // profile screen.
        @Serializable object PatientInfo : PatientRoutes()
        @Serializable data object PatientProfile : PatientRoutes()
        @Serializable data object PatientWeightDialog : PatientRoutes() // dialog.
        @Serializable data object PatientHeightDialog : PatientRoutes() // dialog.
        @Serializable data object PatientSettings : PatientRoutes() // dialog.
        @Serializable data object PatientAppointment : PatientRoutes() // dialog.
    }

    @Serializable
    sealed class DoctorRoutes {

        @Serializable object Doctor : DoctorRoutes()

        // home screens. Расписание на день.
        @Serializable object DocHome : DoctorRoutes()
        @Serializable data object DocDashboard : DoctorRoutes()

        @Serializable object DocPatients : DoctorRoutes()
        @Serializable data object DocPatientsList : DoctorRoutes()
        @Serializable data object DocPatientInfo : DoctorRoutes()
    }

    @Serializable
    sealed class AdminRoutes {

        @Serializable data object AdminDashboard : AdminRoutes()
    }

}

@Composable
fun ApplicationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AuthRoutes = AuthRoutes.Auth,
) {
    val medsPagesVM: MedsPagesViewModel = viewModel()
    val medicationsViewModel: MedicationsViewModel = viewModel()
    val profileVM: ProfileVM = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
            .fillMaxSize()
    ) {
        authGraph(navController)
        patientGraph(
            navController,
            medicationsViewModel,
            medsPagesVM,
            profileVM
        )
        docGraph(
            navController
        )
        // userMedsGraph(navController)
    }
}

// Граф для страниц аутификации.
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthRoutes.Auth>(startDestination = AuthRoutes.Login) {
        composable<AuthRoutes.Login> {
            LoginScreen(
                onLoginClick = { userType ->
                    when (userType) {
                        UserType.PATIENT -> navController.navigate(PatientRoutes.Patient)
                        UserType.DOCTOR -> navController.navigate(DoctorRoutes.Doctor)
                        UserType.ADMIN -> navController.navigate(AdminRoutes.AdminDashboard)
                    }
                },
                onRegistration = {
                    navController.navigate(AuthRoutes.Registration)
                },
                onResetPassword = {
                    navController.navigate(AuthRoutes.PasswordRecovery)
                },
            )
        }

        composable<AuthRoutes.Registration> {
            SignupScreen(
                navigateHome = {
                    navController.popBackStack()
                }
            )
        }

        composable<AuthRoutes.PasswordRecovery> {
            AccountRecoveryScreen(
                navigateHome = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun NavGraphBuilder.docGraph(
    navController: NavHostController
) {
    navigation<DoctorRoutes.Doctor>(startDestination = DoctorRoutes.DocHome) {
        docHomeGraph(navController)


        docPatientsGraph(navController)
        composable<DoctorRoutes.DocPatients> {

        }

    }
}

fun NavGraphBuilder.docPatientsGraph(
    navController: NavHostController
) {
    navigation<DoctorRoutes.DocPatients>(startDestination = DoctorRoutes.DocPatientsList) {
        composable<DoctorRoutes.DocPatientsList> {
            PatientsListScreen()
        }

        composable<DoctorRoutes.DocPatientInfo> {
            /* TODO: Patient Info */
        }
    }
}

fun NavGraphBuilder.docHomeGraph(
    navController: NavHostController
) {
    navigation<DoctorRoutes.DocHome>(startDestination = DoctorRoutes.DocDashboard) {
        composable<DoctorRoutes.DocDashboard> {
            DocDashboardScreen(
                onPatientsClick = {
                    /* navController.navigate(DoctorRoutes.DocPatientInfo) */
                }
            )
        }

    }
}

// Граф для страниц приложения.
fun NavGraphBuilder.patientGraph(
    navController: NavHostController,
    viewModel: MedicationsViewModel,
    medsPagesVM: MedsPagesViewModel,
    profileVM: ProfileVM
) {
    navigation<PatientRoutes.Patient>(startDestination = PatientRoutes.PatientHome) {

        patientDashboardGraph(navController)

        // страница с лекарствами.
        patientMedsGraph(
            navController,
            viewModel,
            medsPagesVM = medsPagesVM
        )

        patientProfileGraph(navController, viewModel = profileVM)

    }
}

fun NavGraphBuilder.patientDashboardGraph(
    navController: NavHostController
) {
    navigation<PatientRoutes.PatientHome>(startDestination = PatientRoutes.PatientTodayMedications) {
        composable<PatientRoutes.PatientTodayMedications> {
            DashboardScreen(
                onMedicationLogsClick = {
                    // open logs history screen.
                    navController.navigate(PatientRoutes.PatientLogs)
                },
            )
        }
        composable<PatientRoutes.PatientLogs> {
            IntakeRecordsScreen(
                onBackClick = {
                    // go back to the dashboard.
                    navController.popBackStack()
                }
            )
        }
    }
}

// Граф для страницы с лекарствами.
fun NavGraphBuilder.patientMedsGraph(
    navController: NavHostController,
    viewModel: MedicationsViewModel,
    medsPagesVM: MedsPagesViewModel
) {
    navigation<PatientRoutes.PatientMedications>(startDestination = PatientRoutes.PatientListMedications) {
        composable<PatientRoutes.PatientListMedications> {
            MedicationsScreen(
                medicationsViewModel = viewModel,
                medsPagesVM = medsPagesVM,
                onAddMedClick = {
                    // Добавление лекарства.
                    navController.navigate(PatientRoutes.PatientAddMedication)
                },
                onViewMed = {
                    // Просмотр лекарства.
                    navController.navigate(PatientRoutes.PatientViewMedication)
                },
                onEditMedClick = { name ->
                    // Редактирование лекарства.
                    navController.navigate(PatientRoutes.PatientUpdateMedication(name))
                },
            )
        }

        composable<PatientRoutes.PatientAddMedication> {
            NewMedicationDataScreen(
                onConfirmClick = {
                    navController.navigate(PatientRoutes.PatientMedications)
                },
            )
        }

        composable<PatientRoutes.PatientViewMedication> {
            ViewMedicationInfoScreen(
                medsViewModel = medsPagesVM,
                onReturn = {
                    navController.popBackStack()
                }
            )
        }

        composable<PatientRoutes.PatientUpdateMedication> { backStackEntry ->
            val args = backStackEntry.toRoute<PatientRoutes.PatientUpdateMedication>()

            UpdateMedScreen(
                passedMedName = args.medicationName ?: "",
                onConfirmEdit = {
                    navController.popBackStack()
                }
            )
        }

    }
}

fun NavGraphBuilder.patientProfileGraph(
    navController: NavHostController,
    viewModel: ProfileVM
) {
    navigation<PatientRoutes.PatientInfo>(startDestination = PatientRoutes.PatientProfile) {
        composable<PatientRoutes.PatientProfile> {
            ProfileScreen(
                viewModel = viewModel,
                onHeightClick = {
                    navController.navigate(PatientRoutes.PatientHeightDialog)
                },
                onWeightClick = {
                    navController.navigate(PatientRoutes.PatientWeightDialog)
                },
                onDoctorClick = {
                    navController.navigate(PatientRoutes.PatientAppointment)
                }

            )
        }

        composable<PatientRoutes.PatientAppointment> {
            AppointmentScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigate(PatientRoutes.PatientProfile)
                }
            )
        }
        dialog<PatientRoutes.PatientWeightDialog> {
            // TODO: implement weight dialog.
        }
        dialog<PatientRoutes.PatientHeightDialog> {
            // TODO: implement height dialog.
        }

    }
}
