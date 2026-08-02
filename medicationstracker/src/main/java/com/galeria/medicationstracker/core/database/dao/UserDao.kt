package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.galeria.medicationstracker.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

  @Upsert
  suspend fun upsertUser(user: UserEntity)

  @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
  suspend fun getUserById(id: String): UserEntity?

  @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
  fun getUserFlow(id: String): Flow<UserEntity?>
}
