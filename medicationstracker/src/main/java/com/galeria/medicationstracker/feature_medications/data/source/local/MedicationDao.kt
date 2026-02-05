package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)
    
    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)
    
    @Update
    suspend fun updateMedication(medication: MedicationEntity)
    
    @Query("SELECT * FROM medication")
    fun getAllMedications(): Flow<List<MedicationEntity>>
    
    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: Int): MedicationEntity?
}

