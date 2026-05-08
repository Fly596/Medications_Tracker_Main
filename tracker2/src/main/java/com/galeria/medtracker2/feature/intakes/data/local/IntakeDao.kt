package com.galeria.medtracker2.feature.intakes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Dao
interface IntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(intake: IntakeLogEntity)

    @Query("DELETE FROM intakes WHERE id = :id")
    suspend fun deleteIntakeById(id: UUID)

    @Query("SELECT * FROM intakes")
    fun getAllIntakes(): Flow<List<IntakeLogEntity>>

    @Query("SELECT * FROM intakes WHERE id = :id")
    suspend fun getIntakeById(id: UUID): IntakeLogEntity?

    // для проверки статуса приема.
    @Query("SELECT status FROM intakes WHERE plannedIntakeId = :plannedIntakeId")
    suspend fun checkIntakeStatus(plannedIntakeId: UUID): Boolean
}