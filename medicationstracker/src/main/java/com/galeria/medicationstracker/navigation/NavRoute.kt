package com.galeria.medicationstracker.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {

  // 1. Auth.
  @Serializable
  data object Login : NavRoute

  @Serializable
  data object Registration : NavRoute

  @Serializable
  data object PasswordRecovery : NavRoute

  // 2. Домашняя страница с приемом лекарств.
  @Serializable
  data object TodayMedications : NavRoute

  @Serializable
  data object MoodCheck : NavRoute

  // 3. Взаимодействие с лекарствами.
  @Serializable
  object MedicationsList : NavRoute

  @Serializable
  object AddMedication : NavRoute

  @Serializable
  data class ViewMedication(val medicationId: String) : NavRoute

  @Serializable
  data class UpdateMedication(val medicationId: String) : NavRoute
}