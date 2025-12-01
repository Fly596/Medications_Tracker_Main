package com.galeria.medicationstracker.data.source.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.galeria.medicationstracker.data.source.local.entities.Intake
import com.galeria.medicationstracker.data.source.local.entities.Mood
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    
    @Query("SELECT * FROM mood")
    fun getAllMoods(): Flow<List<Mood>>
    
    @Query("SELECT * FROM mood WHERE id = :id")
    suspend fun getMoodById(id: Int): Mood?
    
    @Upsert
    suspend fun upsertMood(mood: Mood)
    
    @Update
    suspend fun updateMood(mood: Mood)
    
    @Delete
    suspend fun deleteMood(mood: Intake)
    
}