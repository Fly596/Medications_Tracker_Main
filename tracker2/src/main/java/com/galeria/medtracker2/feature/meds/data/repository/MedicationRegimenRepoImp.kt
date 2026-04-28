package com.galeria.medtracker2.feature.meds.data.repository

import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationRegimenDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeDao
import com.galeria.medtracker2.feature.meds.data.local.schedule.toEntity
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain
import com.galeria.medtracker2.feature.meds.domain.ScheduledDateTimeDomain
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
}
