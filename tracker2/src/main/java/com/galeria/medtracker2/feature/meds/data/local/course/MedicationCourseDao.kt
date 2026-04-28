package com.galeria.medtracker2.feature.meds.data.local.course

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Dao
interface MedicationCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationCourse(medicationCourse: MedicationCourseEntity)

    @Query("DELETE FROM medications_regimens WHERE id = :id")
    suspend fun deleteMedicationCourseById(id: UUID)

    @Query("SELECT * FROM medications_regimens")
    fun getAllMedicationCourses(): Flow<List<MedicationCourseEntity>>

    @Query("SELECT * FROM medications_regimens WHERE id = :id")
    suspend fun getMedicationCourseById(id: UUID): MedicationCourseEntity?

}
