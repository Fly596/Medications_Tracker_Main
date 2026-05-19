package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.MedicationCourseDomain
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import kotlinx.coroutines.flow.Flow

interface MedicationsCourseRepository {

    suspend fun addCourse(course: MedicationCourseDomain)

    suspend fun addPlannedIntake(plannedIntake: PlannedIntakeDomain)

    suspend fun addAllPlannedIntakes(plannedIntakes: List<PlannedIntakeDomain>)

    fun getCourses(): Flow<List<MedicationCourseDomain>>

    fun getActiveCourses(): Flow<List<MedicationCourseSummary>>

    fun getFullSchedule(): Flow<List<ScheduledIntakeDetails>>
}
