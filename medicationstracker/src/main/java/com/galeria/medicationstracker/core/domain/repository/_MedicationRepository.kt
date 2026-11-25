package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.domain.medications.model.Medication
import kotlinx.coroutines.flow.Flow

interface _MedicationRepository {

    suspend fun addMedication(medication: Medication): Result<String>
    fun getMedicationsFlow(): Flow<List<Medication>>
    suspend fun getMedication(medicationId: String): Result<Medication?>
    suspend fun getMedications(): List<Medication>
    fun observeMedication(medicationId: String): Flow<Medication?>
    suspend fun updateMedication(medication: Medication)
    suspend fun deleteMedication(medicationId: String)
}
