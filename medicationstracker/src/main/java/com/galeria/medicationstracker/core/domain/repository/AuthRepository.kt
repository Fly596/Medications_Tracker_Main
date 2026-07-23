package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  // Наблюдение за состоянием аутентификации (null — не залогинен)
  val authStateFlow: Flow<User?>

  // Вход по логину и паролю
  suspend fun signIn(email: String, pass: String): Result<User>

  // Регистрация с сохранением имени пользователя
  suspend fun signUp(email: String, pass: String, name: String): Result<User>
}