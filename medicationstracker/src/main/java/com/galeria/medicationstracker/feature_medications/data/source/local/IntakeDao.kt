package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(intake: IntakeEntity)

    @Delete suspend fun deleteIntake(intake: IntakeEntity)

    @Query("SELECT * FROM intakes")
    fun getAllIntakes(): Flow<List<IntakeEntity>>

    @Query("SELECT * FROM intakes WHERE id = :id")
    suspend fun getIntakeById(id: Int): IntakeEntity?
}
