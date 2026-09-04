package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.DosagePresetEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface DosagePresetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: DosagePresetEntity): Long

    @Delete
    suspend fun delete(preset: DosagePresetEntity)

    @Query(
        """
        SELECT *
        FROM dosage_presets
        WHERE medicationId = :medicationId
        ORDER BY amount
        """
    )
    fun observeForMedication(
        medicationId: UUID
    ): Flow<List<DosagePresetEntity>>
}