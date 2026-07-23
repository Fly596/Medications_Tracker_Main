package com.galeria.medicationstracker.data._repository

import com.galeria.medicationstracker.core.database.dao.UserDao
import com.galeria.medicationstracker.core.domain.model.User
import com.galeria.medicationstracker.core.domain.repository.UserRepository
import com.galeria.medicationstracker.core.firebase.datasource.UserDatasource
import com.galeria.medicationstracker.data.toDomain
import com.galeria.medicationstracker.data.toEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class _UserRepositoryImpl @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val userRemote: UserDatasource,
  private val userDao: UserDao
) : UserRepository {

  private val scope = CoroutineScope(Dispatchers.IO)

  override suspend fun getCurrentUser(): User? {
    val currentUserId = firebaseAuth.currentUser?.uid ?: return null

    // Запускаем фоновую синхронизацию данных с Firestore
    syncWithRemote(currentUserId)

    return userDao.getUserById(currentUserId)?.toDomain()
  }

  private fun syncWithRemote(userId: String) {
    scope.launch {
      // Подписка на изменения документа профиля
      launch {
        userRemote.getUserFlow(userId).collect { doc ->
          doc?.let { userDao.upsertUser(it.toEntity()) }
        }
      }
    }
  }
}