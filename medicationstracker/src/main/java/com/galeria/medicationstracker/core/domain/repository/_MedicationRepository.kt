package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.core.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface _MedicationRepository {

  suspend fun addMedication(userId: String, medication: Medication): Result<String>
  fun getMedicationsFlow(userId: String): Flow<List<Medication>>
  suspend fun getMedications(userId: String): List<Medication>
  fun getMedication(medicationId: String): Flow<Medication?>
  suspend fun updateMedication(userId: String, medication: Medication)
  suspend fun deleteMedication(userId: String, medicationId: String)
}
