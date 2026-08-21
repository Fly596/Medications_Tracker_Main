package com.galeria.medicationstracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.ResetPasswordScreen
import com.galeria.medicationstracker.ui.screens.auth.login.LoginScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DailyMedsScreen
import com.galeria.medicationstracker.ui.screens.dashboard.moodtracker.MoodTrackerScreen

@Composable
fun MainNavHost() {
  val navigationState = NavRoute.Login.navigationState()
  val navigator = remember { AppNavigator(navigationState) }

  Scaffold() { innerPadding ->
    NavDisplay(
      modifier = Modifier.padding(innerPadding),
      backStack = navigationState.stacksInUse,
      onBack = {
        navigator.popBack()
      },
      entryProvider = { key ->
        when (key) {
          // 1. Auth.
          is NavRoute.Login -> {
            NavEntry(key) {
              LoginScreen(
                onLoginSuccessNavigation = {
                  navigator.navigateTo(NavRoute.TodayMedications)
                },
                onRegistration = {
                  navigator.navigateTo(NavRoute.Registration)
                },
                onResetPassword = {
                  navigator.navigateTo(NavRoute.PasswordRecovery)
                },
              )
            }
          }

          is NavRoute.Registration -> {
            NavEntry(key) {
              SignupScreen(navigateHome = { navigator.popBack() })
            }
          }

          is NavRoute.PasswordRecovery -> {
            NavEntry(key) {
              ResetPasswordScreen(navigateHome = { navigator.popBack() })
            }
          }

          // 2. Home page.
          is NavRoute.TodayMedications -> {
            NavEntry(key) {
              DailyMedsScreen(
                onAddMood = { navigator.navigateTo(NavRoute.MoodCheck) },
                onAddIntake = {},
              )
            }
          }

          is NavRoute.MoodCheck -> {
            NavEntry(key) {
              MoodTrackerScreen(onBackClick = { navigator.popBack() })

            }
          }

          // 3. Взаимодействие с лекарствами.
          is NavRoute.MedicationsList -> {
            NavEntry(key) {
              // MedicationsListScreen()
            }
          }

          is NavRoute.AddMedication -> {
            NavEntry(key) {
              // AddMedicationScreen()
            }
          }

          is NavRoute.ViewMedication -> {
            NavEntry(key) {
              // ViewMedicationScreen()
            }
          }

          is NavRoute.UpdateMedication -> {
            NavEntry(key) {
              // UpdateMedicationScreen()
            }
          }

          else -> throw RuntimeException("Unknown route")
        }
      }
    )
  }
}