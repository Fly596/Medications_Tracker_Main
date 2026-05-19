package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.IntakeLogEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Dao
interface IntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(intake: IntakeLogEntity)

    @Query("UPDATE intake_logs SET isTaken = :status WHERE plannedIntakeId =:plannedId")
    suspend fun updateStatus(status: Boolean, plannedId: UUID)

    @Query("DELETE FROM intake_logs WHERE id = :id")
    suspend fun deleteById(id: UUID)

    // для проверки статуса приема.
    @Query("SELECT isTaken FROM intake_logs WHERE plannedIntakeId = :plannedId")
    suspend fun getStatusByPlannedId(plannedId: UUID): Boolean

    // для проверки наличия приема.
    @Query("SELECT EXISTS(SELECT 1 FROM intake_logs WHERE plannedIntakeId = :plannedId)")
    suspend fun existsByPlannedId(plannedId: UUID): Boolean

    @Query("SELECT * FROM intake_logs")
    fun getAllIntakes(): Flow<List<IntakeLogEntity>>

}