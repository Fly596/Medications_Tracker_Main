package com.galeria.medicationstracker.data.source.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.galeria.medicationstracker.data.source.local.entities.Intake
import kotlinx.coroutines.flow.Flow

@Dao
interface IntakeDao {
    
    @Query("SELECT * FROM intake")
    fun getAllIntakes(): Flow<List<Intake>>
    
    @Query("SELECT * FROM intake WHERE id = :id")
    suspend fun getIntakeById(id: Int): Intake?
    
    @Upsert
    suspend fun upsertIntake(intake: Intake)
    
    @Update
    suspend fun updateIntake(intake: Intake)
    
    @Delete
    suspend fun deleteIntake(intake: Intake)
    
}