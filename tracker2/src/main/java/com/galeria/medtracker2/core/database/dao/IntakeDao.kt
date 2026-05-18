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

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertIntake(intake: IntakeLogEntity)

    @Query("UPDATE intake_logs SET isTaken = :intakeStatus WHERE plannedIntakeId =:id")
    suspend fun update(intakeStatus: Boolean, id: UUID)

    /*
    * @Query("UPDATE orders SET order_desc = :description, order_title= :title WHERE order_id =:id")
    void update(String description, String title, int id);
    */
    @Query("DELETE FROM intake_logs WHERE id = :id")
    suspend fun deleteIntakeById(id: UUID)

    @Query("SELECT * FROM intake_logs")
    fun getAllIntakes(): Flow<List<IntakeLogEntity>>

    @Query("SELECT * FROM intake_logs WHERE id = :id")
    suspend fun getIntakeById(id: UUID): IntakeLogEntity?

    // для проверки статуса приема.
    @Query("SELECT isTaken FROM intake_logs WHERE plannedIntakeId = :plannedIntakeId")
    suspend fun checkIntakeStatus(plannedIntakeId: UUID): Boolean

    // для проверки наличия приема.
    @Query("SELECT 1 FROM intake_logs WHERE plannedIntakeId = :plannedIntakeId")
    suspend fun checkIntakeExist(plannedIntakeId: UUID): Boolean
}