package com.galeria.medtracker2.feature.meds.domain

import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.data.local.schedule.PlannedIntakeEntity
import com.galeria.medtracker2.feature.meds.data.local.schedule.RegimentWithNameDoseDate
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MedicationRegimenRepo {

    suspend fun addRegiment(regiment: MedicationCourseDomain)

    suspend fun addSchedule(schedule: PlannedIntakeDomain)

    fun getRegiments(): Flow<List<MedicationCourseDomain>>

    fun getRegimentDateTimeById(id: UUID): Flow<PlannedIntakeEntity>

    fun getRegimentsWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>>

    fun getFullSchedule(): Flow<List<FullSchedule>>
}
