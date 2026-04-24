package com.galeria.medtracker2.feature.meds.data.local.intakes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Dao
@OptIn(ExperimentalUuidApi::class)
interface ScheduledDateTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduledDateTime(scheduledDateTime: ScheduledDateTimeEntity)

    @Query("DELETE FROM scheduled_date_times WHERE id = :id")
    suspend fun deleteScheduledDateTimeById(id: UUID)

    @Query("SELECT * FROM scheduled_date_times")
    fun getAllScheduledDateTimes(): Flow<List<ScheduledDateTimeEntity>>

    @Query("SELECT * FROM scheduled_date_times WHERE id = :id")
    suspend fun getScheduledDateTimeById(id: Uuid): ScheduledDateTimeEntity?
}