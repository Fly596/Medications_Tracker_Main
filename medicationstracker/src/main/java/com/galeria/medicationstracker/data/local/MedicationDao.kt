package com.galeria.medicationstracker.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    
    @Query("SELECT * FROM medication")
    fun getAllMedications(): Flow<List<Medication>>
    
    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: Int): Medication?
    
    @Upsert
    suspend fun upsertMedication(medication: Medication)
    
    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteMedicationById(id: Int)
}