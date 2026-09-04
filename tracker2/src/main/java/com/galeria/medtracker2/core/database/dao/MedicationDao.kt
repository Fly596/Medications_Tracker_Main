package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.galeria.medtracker2.core.database.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medication: MedicationEntity)

    @Update()
    suspend fun update(medication: MedicationEntity)

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getById(id: UUID): MedicationEntity?

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<MedicationEntity>>
}
