package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun deleteMedicationById(id: UUID)

    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: UUID): MedicationEntity?

    @Query("SELECT * FROM medications WHERE name = :name COLLATE NOCASE LIMIT 1")
    suspend fun getMedicationByName(name: String): MedicationEntity?
}
