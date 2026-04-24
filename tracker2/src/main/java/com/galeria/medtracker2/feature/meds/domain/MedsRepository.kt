package com.galeria.medtracker2.feature.meds.domain

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MedsRepository {

    suspend fun addMedication(
        name: String,
    )

    suspend fun removeMedication(
        medicationId: UUID
    )

    suspend fun getMedication(
        medicationId: UUID
    ): DomainMedication

    fun getAllMedications(): Flow<List<DomainMedication>>
}
