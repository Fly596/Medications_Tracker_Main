package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface _AuthRepository {

  // Наблюдение за состоянием аутентификации (null — не залогинен)
  val authStateFlow: Flow<User?>

  // Вход по логину и паролю
  suspend fun signIn(email: String, pass: String): Result<User>

  // Регистрация с сохранением имени пользователя
  suspend fun signUp(email: String, pass: String, name: String, birthDate: LocalDate): Result<User>
}