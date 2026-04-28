package com.galeria.medtracker2.feature.meds.domain

interface MedicationRegimenRepo {

    suspend fun addRegiment(regiment: MedicationRegimentDomain)

    suspend fun addSchedule(schedule: ScheduledDateTimeDomain)
}
