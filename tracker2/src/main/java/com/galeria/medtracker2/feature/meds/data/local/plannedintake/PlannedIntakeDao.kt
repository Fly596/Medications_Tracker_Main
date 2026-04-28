package com.galeria.medtracker2.feature.meds.data.local.plannedintake

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@Dao
@OptIn(ExperimentalUuidApi::class)
interface PlannedIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduledDateTime(scheduledDateTime: PlannedIntakeEntity)

    @Query("DELETE FROM scheduled_date_times WHERE id = :id")
    suspend fun deleteScheduledDateTimeById(id: UUID)

    @Query("SELECT * FROM scheduled_date_times")
    fun getAllScheduledDateTimes(): Flow<List<PlannedIntakeEntity>>

    @Query("SELECT * FROM scheduled_date_times WHERE id = :id")
    suspend fun getScheduledDateTimeById(id: UUID): PlannedIntakeEntity?
}