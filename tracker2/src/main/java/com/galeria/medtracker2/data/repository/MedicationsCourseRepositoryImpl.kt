package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.MedicationCourseDao
import com.galeria.medtracker2.core.database.dao.PlannedIntakeDao
import com.galeria.medtracker2.core.database.dao.ScheduleDao
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.MedicationCourseDomain
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicationsCourseRepositoryImpl
@Inject
constructor(
    private val medicationCourseDao: MedicationCourseDao,
    private val plannedIntakeDao: PlannedIntakeDao,
    private val scheduleDao: ScheduleDao,
) : MedicationsCourseRepository {

    override suspend fun addCourse(course: MedicationCourseDomain) {

        medicationCourseDao.upsert(course.toEntity())
    }

    override suspend fun addPlannedIntake(plannedIntake: PlannedIntakeDomain) {
        plannedIntakeDao.upsert(plannedIntake.toEntity())
    }

    override suspend fun addAllPlannedIntakes(plannedIntakes: List<PlannedIntakeDomain>) {
        val entities = plannedIntakes.map { it ->
            it.toEntity()
        }
        plannedIntakeDao.insertBatch(entities)
    }

    override fun getCourses(): Flow<List<MedicationCourseDomain>> =
        medicationCourseDao.getAllMedicationCourses().map { regimentsList ->
            regimentsList.map { it.toDomain() }
        }

    // Возвращает совмещенную таблицу с именем и датами начала и конца приема.
    override fun getActiveCourses(): Flow<List<MedicationCourseSummary>> =
        scheduleDao.getActiveCoursesStream()

    // Возвращает совмещенную таблицу вмсех приемов по времени.
    override fun getFullSchedule(): Flow<List<ScheduledIntakeDetails>> =
        scheduleDao.getFullScheduleStream()
}
