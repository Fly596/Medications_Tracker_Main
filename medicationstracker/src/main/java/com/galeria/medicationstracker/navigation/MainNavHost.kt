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
import com.galeria.medicationstracker.feature.auth.ui.ALoginScreen
import com.galeria.medicationstracker.feature.auth.ui.ResetPasswordScreen
import com.galeria.medicationstracker.feature.auth.ui.SignupScreen
import com.galeria.medicationstracker.feature.medications.ui.AddMedicationScreen
import com.galeria.medicationstracker.feature.medications.ui.DailyMedsScreen
import com.galeria.medicationstracker.feature.medications.ui.MedicationInfoScreen
import com.galeria.medicationstracker.feature.medications.ui.MedicationsScreen
import com.galeria.medicationstracker.feature.medications.ui.MoodTrackerScreen
import com.galeria.medicationstracker.feature.medications.ui.UpdateMedScreen
import com.galeria.medicationstracker.feature.profile.ui.NewNoteScreen
import com.galeria.medicationstracker.feature.profile.ui.ProfileDetailsScreen
import com.galeria.medicationstracker.feature.profile.ui.ProfileScreen

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val navigationState = NavRoute.TodayMedications.navigationState()
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
        },
        modifier = modifier
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
                            AddMedicationScreen(
                                navigateBack = { navigator.popBack() }
                            )
                        }
                    }

                    is NavRoute.ViewMedication -> {
                        NavEntry(key) {
                            MedicationInfoScreen(
                                medicationId = key.medicationId,
                                onNavigateToMedsList = { navigator.popBack() }
                            )
                        }
                    }

                    is NavRoute.UpdateMedication -> {
                        NavEntry(key) {
                            UpdateMedScreen(
                                medicationId = key.medicationId,
                            )
                        }
                    }

                    is NavRoute.Profile -> {
                        NavEntry(key) {
                            ProfileScreen(
                                onEditProfileClick = {
                                    navigator.navigateTo(NavRoute.ProfileOverview)
                                },
                                onNotesClick = { navigator.navigateTo(NavRoute.Notes) },
                            )
                        }
                    }

                    is NavRoute.ProfileOverview -> {
                        NavEntry(key) {
                            ProfileDetailsScreen(
                            )
                        }
                    }

                    is NavRoute.Notes -> {
                        NavEntry(key) {
                            NewNoteScreen(
                            )
                        }
                    }

                    else -> throw RuntimeException("Unknown route")
                }
            }
        )
    }
}