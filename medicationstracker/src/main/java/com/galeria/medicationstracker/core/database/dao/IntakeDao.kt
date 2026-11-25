package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.galeria.medicationstracker.core.database.entity.IntakeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for handling [IntakeEntity] operations in Room.
 */
@Dao
interface IntakeDao : BaseDao<IntakeEntity> {

    @Query("SELECT * FROM intakes WHERE id = :id")
    suspend fun getIntakeById(id: String): IntakeEntity?

    @Query("SELECT * FROM intakes WHERE medicationId = :medicationId ORDER BY intakeTime DESC")
    fun observeIntakesForMedication(medicationId: String): Flow<List<IntakeEntity>>

    @Query("SELECT * FROM intakes WHERE medicationId = :medicationId ORDER BY intakeTime DESC")
    suspend fun getIntakesForMedication(medicationId: String): List<IntakeEntity>

    @Query("DELETE FROM intakes WHERE medicationId = :medicationId")
    suspend fun deleteIntakesForMedication(medicationId: String)

    @Query("DELETE FROM intakes")
    suspend fun clearAll()
}
