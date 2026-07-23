package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.core.domain.model.User

interface _UserRepository {

  // Поток текущих данных пользователя
  //fun getCurrentUserFlow(): Flow<User?>
  suspend fun getCurrentUser(): User?
}