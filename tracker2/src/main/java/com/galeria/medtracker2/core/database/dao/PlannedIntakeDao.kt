package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@Dao
@OptIn(ExperimentalUuidApi::class)
interface PlannedIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(plannedIntakes: List<PlannedIntakeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(plannedIntake: PlannedIntakeEntity)

    @Query("SELECT * FROM planned_intakes")
    fun getAllPlannedIntakes(): Flow<List<PlannedIntakeEntity>>

    @Query("SELECT * FROM planned_intakes WHERE id = :id")
    suspend fun getById(id: UUID): PlannedIntakeEntity?
}
