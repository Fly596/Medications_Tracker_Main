package com.galeria.medtracker2.feature.meds.domain

import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.data.local.schedule.RegimentWithNameDoseDate
import com.galeria.medtracker2.feature.meds.data.local.schedule.ScheduledDateTimeEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MedicationRegimenRepo {

    suspend fun addRegiment(regiment: MedicationRegimentDomain)

    suspend fun addSchedule(schedule: ScheduledDateTimeDomain)

    fun getRegiments(): Flow<List<MedicationRegimentDomain>>

    fun getRegimentDateTimeById(id: UUID): Flow<ScheduledDateTimeEntity>

    fun getRegimentsWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>>

    fun getFullSchedule(): Flow<List<FullSchedule>>
}
