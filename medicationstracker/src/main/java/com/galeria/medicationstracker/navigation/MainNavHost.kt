package com.galeria.medicationstracker.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.galeria.medicationstracker.feature.auth.login.ALoginScreen
import com.galeria.medicationstracker.ui.screens.auth.accountrecovery.ResetPasswordScreen
import com.galeria.medicationstracker.ui.screens.auth.signup.SignupScreen
import com.galeria.medicationstracker.ui.screens.dashboard.DailyMedsScreen
import com.galeria.medicationstracker.ui.screens.dashboard.moodtracker.MoodTrackerScreen
import com.galeria.medicationstracker.ui.screens.medications.MedicationsScreen
import com.galeria.medicationstracker.ui.screens.medications.mediinfo.ViewMedicationInfoScreen
import com.galeria.medicationstracker.ui.screens.medications.newmed.NewMedicationDataScreen
import com.galeria.medicationstracker.ui.screens.profile.UserProfileScreen

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
  val navigationState = NavRoute.Login.navigationState()
  val navigator = remember { AppNavigator(navigationState) }

  // 1. Достаем текущий роут из вершины стэка
  val currentRoute = navigationState.stacksInUse.lastOrNull() as? NavRoute
  val isBottomBarVisible = currentRoute?.shouldShowBottomBar == true

  Scaffold(
    bottomBar = {
      // 2. Анимированное скрытие / показ
      AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
      ) {
        NavigationBar {
          TopLevelTab.entries.forEach { tab ->
            val isSelected = currentRoute == tab.route

            NavigationBarItem(
              selected = isSelected,
              onClick = {
                if (!isSelected) {
                  // Очищаем стэк до рута вкладки, чтоб не плодить мусор
                  navigator.navigateAndClear(tab.route)
                }
              },
              icon = {
                Icon(
                  painter = painterResource(id = tab.icon),
                  contentDescription = tab.title
                )
              },
              label = { Text(text = tab.title) }
            )
          }
        }
      }
    }
  ) { innerPadding ->
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
              ALoginScreen(
                onNavigateToHome = {
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
              MedicationsScreen(
                onAddClick = {
                  navigator.navigateTo(NavRoute.AddMedication)
                },
                onViewClick = { id ->
                  navigator.navigateTo(NavRoute.ViewMedication(id))
                },
                onEditClick = { id ->
                  navigator.navigateTo(NavRoute.UpdateMedication(id))
                },
              )
            }
          }

          is NavRoute.AddMedication -> {
            NavEntry(key) {
              NewMedicationDataScreen(
                navigateBack = { navigator.popBack() }
              )
            }
          }

          is NavRoute.ViewMedication -> {
            NavEntry(key) {
              ViewMedicationInfoScreen(
                onNavigateToMedsList = { navigator.popBack() }
              )
            }
          }

          is NavRoute.UpdateMedication -> {
            NavEntry(key) {
              // UpdateMedicationScreen()
            }
          }

          is NavRoute.Profile -> {
            NavEntry(key) {
              UserProfileScreen(
                onEditProfileClick = {
                  navigator.navigateTo(NavRoute.ProfileOverview)
                },
                onNotesClick = { navigator.navigateTo(NavRoute.Notes) },
              )
            }
          }

          else -> throw RuntimeException("Unknown route")
        }
      }
    )
  }
}