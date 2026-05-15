package com.galeria.medtracker2.feature.meds.domain

import com.galeria.medtracker2.core.common.data.MedicationCourseSummary
import com.galeria.medtracker2.core.common.data.ScheduledIntakeDetails
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeEntity
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
