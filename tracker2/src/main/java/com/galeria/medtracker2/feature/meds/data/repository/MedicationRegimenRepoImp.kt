package com.galeria.medtracker2.feature.meds.data.repository

import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationRegimenDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.RegimentWithNameDoseDate
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeEntity
import com.galeria.medtracker2.feature.meds.data.local.schedule.toDomain
import com.galeria.medtracker2.feature.meds.data.local.schedule.toEntity
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain
import com.galeria.medtracker2.feature.meds.domain.ScheduledDateTimeDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MedicationRegimenRepoImp
@Inject
constructor(
    private val medicationRegimenDao: MedicationRegimenDao,
    private val scheduledDateTimeDao: ScheduledDateTimeDao,
) : MedicationRegimenRepo {

    override suspend fun addRegiment(regiment: MedicationRegimentDomain) {

        medicationRegimenDao.insertMedicationRegimen(regiment.toEntity())
    }

    override suspend fun addSchedule(schedule: ScheduledDateTimeDomain) {
        scheduledDateTimeDao.insertScheduledDateTime(schedule.toEntity())
    }

    override fun getRegiments(): Flow<List<MedicationRegimentDomain>> =
        medicationRegimenDao.getAllMedicationRegimens().map { regimentsList ->
            regimentsList.map { it.toDomain() }
        }

    //
    override fun getRegimentDateTimeById(
        id: UUID
    ): Flow<ScheduledDateTimeEntity> {
        TODO("Not yet implemented")
    }

    // Возвращает совмещенную таблицу с именем и датами начала и конца приема.
    override fun getRegimentsWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>> =
        medicationRegimenDao.getRegimentWithNameDoseDates()

    // Возвращает совмещенную таблицу вмсех приемов по времени.
    override fun getFullSchedule(): Flow<List<FullSchedule>> =
        medicationRegimenDao.getFullScheduleDateTimes()

}
