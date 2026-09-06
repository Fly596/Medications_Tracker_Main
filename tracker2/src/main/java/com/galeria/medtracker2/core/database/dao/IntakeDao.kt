package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.IntakeEntity
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

    @Query("SELECT SUM(amount) as total FROM intakes WHERE medicationId = :medicationId")
    fun getTotalDosage(medicationId: UUID): Flow<Double>
}