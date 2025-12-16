package com.galeria.medtracker2.feature_meds.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteMedicationById(id: String)

    @Query("SELECT * FROM medication")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: String): MedicationEntity?
}
