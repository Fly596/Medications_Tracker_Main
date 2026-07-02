package com.galeria.medtracker2.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medtracker2.core.database.entity.MedicationCourseEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Dao
interface MedicationCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(medicationCourse: MedicationCourseEntity)

    @Query("DELETE FROM medication_courses WHERE id = :id")
    suspend fun deleteMedicationCourseById(id: UUID)

    @Query("SELECT * FROM medication_courses")
    fun getAllMedicationCourses(): Flow<List<MedicationCourseEntity>>

    @Query("SELECT * FROM medication_courses WHERE id = :id")
    suspend fun getMedicationCourseById(id: UUID): MedicationCourseEntity?

    // TODO: проверить.
    @Query("SELECT * FROM medication_courses WHERE medicationId = :medId")
    suspend fun getMedicationCourseByMedId(medId: UUID): MedicationCourseEntity
}
