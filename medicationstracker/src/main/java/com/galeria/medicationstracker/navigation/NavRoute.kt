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

  @Serializable
  object Profile : NavRoute

  @Serializable
  object ProfileOverview : NavRoute

  @Serializable
  object Notes : NavRoute
}

// Расширение, определяющее, нужно ли показывать BottomBar для экрана
val NavRoute.shouldShowBottomBar: Boolean
  get() = when (this) {
    is NavRoute.Login,
    is NavRoute.Registration,
    is NavRoute.PasswordRecovery -> false

    // Показываем на основных экранах
    is NavRoute.TodayMedications,
    is NavRoute.MedicationsList,
    is NavRoute.Profile -> true

    // На детальных экранах обычно скрывают, чтобы освободить место
    is NavRoute.MoodCheck,
    is NavRoute.AddMedication,
    is NavRoute.ViewMedication,
    is NavRoute.UpdateMedication,
    is NavRoute.Notes,
    is NavRoute.ProfileOverview,
      -> false

  }
