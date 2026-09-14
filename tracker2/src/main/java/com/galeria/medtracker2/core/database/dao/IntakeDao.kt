package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.IntakeEntity
import com.galeria.medtracker2.domain.model.MedicationStats
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface IntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intake: IntakeEntity): Long

    @Query("SELECT * FROM intakes ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<IntakeEntity>>

    @Query("SELECT * FROM intakes WHERE id = :intakeId")
    suspend fun getById(intakeId: Long): IntakeEntity?

    @Query("DELETE FROM intakes WHERE id = :intakeId")
    suspend fun deleteById(intakeId: Long)

    @Query(
        """
        SELECT
            COALESCE(SUM(amount), 0) AS totalDosage,
            COALESCE(SUM(priceCents), 0) AS totalSpent
        FROM intakes
        WHERE medicationId = :medicationId
    """
    )
    fun getTotalStats(medicationId: UUID): Flow<MedicationStats>

    @Query(
        """
        SELECT timestamp AS intakeDateTime
        FROM intakes 
        WHERE medicationId = :medicationId 
        ORDER BY timestamp DESC 
        LIMIT 1
    """
    )
    suspend fun getLatestIntakeTimestamp(medicationId: UUID): Long?
}