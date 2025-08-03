package com.galeria.medicationstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    
    @Insert(onConflict = REPLACE)
    suspend fun insertUser(user: User)
    
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int)
    
    @Query("SELECT * FROM user WHERE networkId = :networkId")
    suspend fun getUserByNetworkId(networkId: String)
}