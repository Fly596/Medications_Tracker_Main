package com.galeria.medicationstracker.data._repository

import com.galeria.medicationstracker.core.database.dao.UserDao
import com.galeria.medicationstracker.core.domain.model.User
import com.galeria.medicationstracker.core.domain.repository._AuthRepository
import com.galeria.medicationstracker.core.firebase.datasource.AuthDatasource
import com.galeria.medicationstracker.core.firebase.datasource.UserDatasource
import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.galeria.medicationstracker.data.toDomain
import com.galeria.medicationstracker.data.toEntity
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class _AuthRepositoryImpl @Inject constructor(
  private val authRemote: AuthDatasource,
  private val userRemote: UserDatasource,
  private val userDao: UserDao
) : _AuthRepository {

  // Следим за состоянием сессии
  override val authStateFlow: Flow<User?> = authRemote.authStateFlow
    .map { firebaseUser ->
      if (firebaseUser == null) {
        null
      } else {
        // Если пользователь авторизован, пытаемся взять его данные из локального кеша
        userDao.getUserById(firebaseUser.uid)?.toDomain()
      }
    }

  override suspend fun signIn(email: String, pass: String): Result<User> = runCatching {
    // 1. Авторизуемся через Firebase Auth
    val firebaseUser = authRemote.signIn(email, pass)

    // 2. Фетчим профиль пользователя из Firestore
    val userDoc = userRemote.getUserData(firebaseUser.uid)
      ?: throw IllegalStateException("Профиль пользователя не найден в Firestore")

    // 3. Сохраняем полученные данные в локальную база данных Room
    val userEntity = userDoc.toEntity()
    userDao.upsertUser(userEntity)

    // 4. Возвращаем готовую Domain-модель
    userEntity.toDomain()
  }

  override suspend fun signUp(
    email: String,
    pass: String,
    name: String,
    birthDate: LocalDate
  ): Result<User> =
      runCatching {
        // 1. Регистрируем пользователя в Firebase Auth
        val firebaseUser = authRemote.signUp(email, pass)

        // 2. Формируем DTO для Firestore
        val userDoc = UserDocument(
          id = firebaseUser.uid,
          email = email,
          name = name,
          dateOfBirth = Timestamp(birthDate.toEpochDay(), 0)
        )

        // 3. Сохраняем профиль в Firestore
        userRemote.addUser(userDoc)

        // 4. Кешируем созданный профиль в локальную БД Room
        val userEntity = userDoc.toEntity()
        userDao.upsertUser(userEntity)

        // 5. Возвращаем Domain-модель
        userEntity.toDomain()
      }
}