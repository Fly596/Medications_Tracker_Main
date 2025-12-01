package com.galeria.medicationstracker.data.source.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medicationstracker.data.source.local.entities.User
import com.galeria.medicationstracker.data.source.local.entities.UserNote


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertNote(note: UserNote)
    
    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): UserNote?
    
    @Query("SELECT * FROM note WHERE firestoreId = :networkId")
    suspend fun getNoteByNetworkId(networkId: String): UserNote?
}