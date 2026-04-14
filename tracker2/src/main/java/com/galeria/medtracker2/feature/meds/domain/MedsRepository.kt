package com.galeria.medtracker2.feature.meds.domain

import kotlinx.coroutines.flow.Flow

interface MedsRepository {
    suspend fun addMedication(
        name: String,
        doseMg: Double?,
        stock: Double?,
        stockMeasureUnit: String?,
        drugClass: String?
    )
    
    suspend fun removeMedication(
        medicationId: String
    )
    
    suspend fun getMedication (
        medicationId: String
    ): DomainMedication
    
    fun getAllMedications(): Flow<List<DomainMedication>>
}
