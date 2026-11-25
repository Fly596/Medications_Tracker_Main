package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.galeria.medicationstracker.core.database.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?
}
