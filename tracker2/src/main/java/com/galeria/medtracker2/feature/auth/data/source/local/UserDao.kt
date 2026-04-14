package com.galeria.medtracker2.feature.auth.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id") suspend fun getUserById(id: String): UserEntity?

    @Update suspend fun updateUser(user: UserEntity)

    @Delete suspend fun deleteUser(user: UserEntity)
}
