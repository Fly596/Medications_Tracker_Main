package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.galeria.medicationstracker.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUserById(id: String): Flow<UserEntity?>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}
