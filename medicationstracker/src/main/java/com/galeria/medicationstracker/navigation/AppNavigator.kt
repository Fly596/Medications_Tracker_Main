package com.galeria.medicationstracker.navigation

import android.util.Log
import androidx.navigation3.runtime.NavKey

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
