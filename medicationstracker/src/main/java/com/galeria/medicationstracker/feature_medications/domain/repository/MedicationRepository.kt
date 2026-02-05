package com.galeria.medicationstracker.feature_medications.domain.repository

import com.galeria.medicationstracker.feature_medications.domain.model.Medication
import com.galeria.medicationstracker.utils.Response
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    
    fun getMedications(): Flow<Response<List<Medication>>>
    
    fun getMedication(medicationId: String): Flow<Response<Medication>>
    
    suspend fun addMedication(medication: Medication): Response<Unit>
    
    suspend fun deleteMedication(id: String): Response<Unit>
    
    suspend fun updateMedication(medication: Medication): Response<Unit>
}

