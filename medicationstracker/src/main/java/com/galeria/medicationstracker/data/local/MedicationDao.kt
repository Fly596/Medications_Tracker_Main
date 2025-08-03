package com.galeria.medicationstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicationDao {
    
    @Query("SELECT * FROM medication")
    suspend fun getAllMedications(): List<Medication>
    
    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: Int): Medication?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: Medication)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedications(medications: List<Medication>)
    
    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteMedicationById(id: Int)
}