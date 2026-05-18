package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.MedicationCourseDao
import com.galeria.medtracker2.core.database.dao.MedicationScheduleDao
import com.galeria.medtracker2.core.database.dao.PlannedIntakeDao
import com.galeria.medtracker2.core.database.entity.PlannedIntakeEntity
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.MedicationCourseDomain
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import com.galeria.medtracker2.domain.repository.MedicationScheduleIntakesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MedicationScheduleIntakesRepositoryImp
@Inject
constructor(
    private val medicationCourseDao: MedicationCourseDao,
    private val plannedIntakeDao: PlannedIntakeDao,
    private val medicationScheduleDao: MedicationScheduleDao,
) : MedicationScheduleIntakesRepository {

    override suspend fun addCourse(course: MedicationCourseDomain) {

        medicationCourseDao.insertMedicationCourse(course.toEntity())
    }

    override suspend fun addPlannedIntake(plannedIntake: PlannedIntakeDomain) {
        plannedIntakeDao.insertPlannedIntake(plannedIntake.toEntity())
    }

    override suspend fun addAllPlannedIntakes(plannedIntakes: List<PlannedIntakeDomain>) {
        val entities = plannedIntakes.map { it ->
            it.toEntity()
        }
        plannedIntakeDao.insertAll(entities)
    }

    override fun getCourses(): Flow<List<MedicationCourseDomain>> =
        medicationCourseDao.getAllMedicationCourses().map { regimentsList ->
            regimentsList.map { it.toDomain() }
        }

    override fun getPlannedIntakeById(id: UUID): Flow<PlannedIntakeEntity> {
        TODO("Not yet implemented")
    }

    // Возвращает совмещенную таблицу с именем и датами начала и конца приема.
    override fun getCourseSummary(): Flow<List<MedicationCourseSummary>> =
        medicationScheduleDao.getCourseSummaries()

    // Возвращает совмещенную таблицу вмсех приемов по времени.
    override fun getFullSchedule(): Flow<List<ScheduledIntakeDetails>> =
        medicationScheduleDao.getScheduledIntakesWithDetails()
}
