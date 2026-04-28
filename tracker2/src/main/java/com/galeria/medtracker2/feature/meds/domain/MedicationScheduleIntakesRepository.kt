package com.galeria.medtracker2.feature.meds.domain

import com.galeria.medtracker2.feature.meds.data.local.combined.FullSchedule
import com.galeria.medtracker2.feature.meds.data.local.combined.RegimentWithNameDoseDate
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MedicationScheduleIntakesRepository {

    suspend fun addCourse(regiment: MedicationCourseDomain)

    suspend fun addPlannedIntake(schedule: PlannedIntakeDomain)

    fun getCourses(): Flow<List<MedicationCourseDomain>>

    fun getPlannedIntakeById(id: UUID): Flow<PlannedIntakeEntity>

    fun getRegimentsWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>>

    fun getFullSchedule(): Flow<List<FullSchedule>>
}
