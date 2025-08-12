package com.galeria.medicationstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IntakeDao {
    
    @Query("SELECT * FROM intake")
    suspend fun getAllIntakes(): List<Intake>
    
    @Query("SELECT * FROM intake WHERE id = :id")
    suspend fun getIntakeById(id: Int): Intake?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(intake: Intake)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntakes(intakes: List<Intake>)
}
