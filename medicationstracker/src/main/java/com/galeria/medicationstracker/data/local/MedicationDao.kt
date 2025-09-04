package com.galeria.medicationstracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: Medication):Long
    
    @Update
    suspend fun updateMedication(medication: Medication)
    
    @Delete
    suspend fun deleteMedication(medication: Medication)
    
    @Query("SELECT * FROM medication WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getMedicationByFirestoreId(firestoreId: String): Medication?
    
    
    @Query("SELECT * FROM medication")
    fun getAllMedications(): Flow<List<Medication>>

    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: Int): Medication?

    @Upsert suspend fun upsertMedication(medication: Medication)

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteMedicationById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedications(medication: Medication)
}
