package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity
import com.galeria.medtracker2.domain.model.MedicationCourseDomain
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MedicationScheduleIntakesRepository {

    suspend fun addCourse(regiment: MedicationCourseDomain)

    suspend fun addPlannedIntake(schedule: PlannedIntakeDomain)

    fun getCourses(): Flow<List<MedicationCourseDomain>>

    fun getPlannedIntakeById(id: UUID): Flow<PlannedIntakeEntity>

    fun getRegimentsWithNameDoseDates(): Flow<List<MedicationCourseSummary>>

    fun getFullSchedule(): Flow<List<ScheduledIntakeDetails>>
}
