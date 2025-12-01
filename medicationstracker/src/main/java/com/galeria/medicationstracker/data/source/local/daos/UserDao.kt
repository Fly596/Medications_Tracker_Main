package com.galeria.medicationstracker.data.source.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medicationstracker.data.source.local.entities.User

@Dao
interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUser(user: User)
    
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int): User?
    
    // !TODO
    @Query("SELECT * FROM user WHERE firestoreId = :networkId")
    suspend fun getUserByNetworkId(networkId: String): User?
}