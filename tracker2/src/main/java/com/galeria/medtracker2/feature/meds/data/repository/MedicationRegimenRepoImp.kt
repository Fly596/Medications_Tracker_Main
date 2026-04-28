package com.galeria.medtracker2.feature.meds.data.repository

import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationCourseDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.PlannedIntakeDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.PlannedIntakeEntity
import com.galeria.medtracker2.feature.meds.data.local.schedule.RegimentWithNameDoseDate
import com.galeria.medtracker2.feature.meds.data.local.schedule.toDomain
import com.galeria.medtracker2.feature.meds.data.local.schedule.toEntity
import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MedicationRegimenRepoImp
@Inject
constructor(
    private val medicationCourseDao: MedicationCourseDao,
    private val plannedIntakeDao: PlannedIntakeDao,
) : MedicationRegimenRepo {

    override suspend fun addRegiment(regiment: MedicationCourseDomain) {

        medicationCourseDao.insertMedicationRegimen(regiment.toEntity())
    }

    override suspend fun addSchedule(schedule: PlannedIntakeDomain) {
        plannedIntakeDao.insertScheduledDateTime(schedule.toEntity())
    }

    override fun getRegiments(): Flow<List<MedicationCourseDomain>> =
        medicationCourseDao.getAllMedicationRegimens().map { regimentsList ->
            regimentsList.map { it.toDomain() }
        }

    //
    override fun getRegimentDateTimeById(
        id: UUID
    ): Flow<PlannedIntakeEntity> {
        TODO("Not yet implemented")
    }

    // Возвращает совмещенную таблицу с именем и датами начала и конца приема.
    override fun getRegimentsWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>> =
        medicationCourseDao.getRegimentWithNameDoseDates()

    // Возвращает совмещенную таблицу вмсех приемов по времени.
    override fun getFullSchedule(): Flow<List<FullSchedule>> =
        medicationCourseDao.getFullScheduleDateTimes()

}
