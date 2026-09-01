package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.IntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MainIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainIntake(intake: IntakeEntity): Long

    @Query("SELECT * FROM intakes")
    fun getAllMainIntakes(): Flow<List<IntakeEntity>>

    @Query("DELETE FROM intakes WHERE id = :intakeId")
    suspend fun deleteMainIntakeById(intakeId: Int)

    @Query("SELECT * FROM intakes WHERE id = :intakeId")
    suspend fun getMainIntakeById(intakeId: Int): IntakeEntity?
}