package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for handling [MedicationEntity] table operations in Room.
 */
@Dao
interface MedicationDao : BaseDao<MedicationEntity> {

    @Query("SELECT * FROM medications")
    fun observeMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications")
    suspend fun getMedications(): List<MedicationEntity>

    @Query("SELECT * FROM medications WHERE id = :medicationId")
    suspend fun getMedicationById(medicationId: String): MedicationEntity?

    @Query(
        """
        SELECT * FROM medications 
        WHERE :date BETWEEN startDate AND endDate 
          AND daysOfWeek LIKE '%' || :dayOfWeek || '%'
    """
    )
    fun getMedicationsForDate(
        date: LocalDate,
        dayOfWeek: String
    ): Flow<List<MedicationEntity>>

    @Query("DELETE FROM medications WHERE id = :medicationId")
    suspend fun deleteMedicationById(medicationId: String)

    @Query("DELETE FROM medications")
    suspend fun clearAll()
}