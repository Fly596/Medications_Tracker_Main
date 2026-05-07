package com.galeria.medtracker2.feature.meds.data.repository

import com.galeria.medtracker2.core.common.data.FullSchedule
import com.galeria.medtracker2.core.common.data.RegimentWithNameDoseDate
import com.galeria.medtracker2.feature.meds.data.local.combined.CombinedDao
import com.galeria.medtracker2.feature.meds.data.local.course.MedicationCourseDao
import com.galeria.medtracker2.feature.meds.data.local.course.toDomain
import com.galeria.medtracker2.feature.meds.data.local.course.toEntity
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeDao
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.PlannedIntakeEntity
import com.galeria.medtracker2.feature.meds.data.local.plannedintake.toEntity
import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationScheduleIntakesRepository
import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MedicationScheduleIntakesRepositoryImp
@Inject
constructor(
    private val medicationCourseDao: MedicationCourseDao,
    private val plannedIntakeDao: PlannedIntakeDao,
    private val combinedDao: CombinedDao
) : MedicationScheduleIntakesRepository {

    override suspend fun addCourse(regiment: MedicationCourseDomain) {

        medicationCourseDao.insertMedicationCourse(regiment.toEntity())
    }

    override suspend fun addPlannedIntake(schedule: PlannedIntakeDomain) {
        plannedIntakeDao.insertScheduledDateTime(schedule.toEntity())
    }

    override fun getCourses(): Flow<List<MedicationCourseDomain>> =
        medicationCourseDao.getAllMedicationCourses().map { regimentsList ->
            regimentsList.map { it.toDomain() }
        }

    override fun getPlannedIntakeById(
        id: UUID
    ): Flow<PlannedIntakeEntity> {
        TODO("Not yet implemented")
    }

    // Возвращает совмещенную таблицу с именем и датами начала и конца приема.
    override fun getRegimentsWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>> =
        combinedDao.getMedicationsNameDoseDates()

    // Возвращает совмещенную таблицу вмсех приемов по времени.
    override fun getFullSchedule(): Flow<List<FullSchedule>> =
        combinedDao.getFullScheduleDateTimes()

}
