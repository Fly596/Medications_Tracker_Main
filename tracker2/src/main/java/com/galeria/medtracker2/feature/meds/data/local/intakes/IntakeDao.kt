package com.galeria.medtracker2.feature.meds.data.local.intakes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface IntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(intake: IntakeEntity)

    @Query("DELETE FROM intakes WHERE id = :id")
    suspend fun deleteIntakeById(id: Uuid)

    @Query("SELECT * FROM intakes")
    fun getAllIntakes(): Flow<List<IntakeEntity>>

    @Query("SELECT * FROM intakes WHERE id = :id")
    suspend fun getIntakeById(id: Uuid): IntakeEntity?
}