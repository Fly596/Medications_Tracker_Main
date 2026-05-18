package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.MedicationDomain
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Отвечает только за медикаменты, без расписаний.
 */
interface MedicationRepository {

    suspend fun addMedication(
        medication: MedicationDomain
    )

    suspend fun removeMedication(
        medicationId: UUID
    )

    suspend fun getMedication(
        medicationId: UUID
    ): MedicationDomain

    fun getAllMedications(): Flow<List<MedicationDomain>>
}
