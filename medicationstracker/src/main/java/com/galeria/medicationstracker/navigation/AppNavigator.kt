package com.galeria.medicationstracker.navigation

import android.util.Log
import androidx.navigation3.runtime.NavKey
import com.galeria.medicationstracker.R

class AppNavigator(val state: NavigationState) {

  fun navigateTo(navKey: NavKey) {
    state.stacksInUse.add(navKey)
    Log.d("AppNavigator", "Current: ${state.stacksInUse.lastOrNull()}\n")
    Log.d(
      "AppNavigator",
      "Items: ${state.stacksInUse.forEach { Log.d("AppNavigator", "Item: $it") }}"
    )
  }

  fun navigateAndClear(navKey: NavKey) {
    state.stacksInUse.clear()
    state.stacksInUse.add(navKey)
    Log.d("AppNavigator", "Current: ${state.stacksInUse.lastOrNull()}\n")

    Log.d(
      "AppNavigator",
      "Items: ${state.stacksInUse.forEach { Log.d("AppNavigator", "Item: $it") }}"
    )
  }

  fun popBack() {
    state.stacksInUse.removeLastOrNull()

    Log.d("AppNavigator", "Current: ${state.stacksInUse.lastOrNull()}\n")

    Log.d(
      "AppNavigator",
      "Items: ${state.stacksInUse.forEach { Log.d("AppNavigator", "Item: $it") }}"
    )
  }
}

enum class TopLevelTab(
  val route: NavRoute,
  val title: String,
  val icon: Int
) {

  HOME(NavRoute.TodayMedications, "Главная", R.drawable.home_fill),
  MEDICATIONS(
    NavRoute.MedicationsList,
    "Поиск",
    R.drawable.lab_profile_fill
  ),
  PROFILE(NavRoute.Profile, "Профиль", R.drawable.profile_fill)
}
