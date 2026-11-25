package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.domain.medications.model.Medication
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining operations for Medication data access.
 *
 * Suspending operations return [Result] to explicitly communicate operation outcomes.
 * Reactive streams return [Flow].
 */
interface MedicationRepository {

    /**
     * Observes all medications as a reactive stream.
     */
    fun getMedicationsFlow(): Flow<List<Medication>>

    /**
     * Retrieves all medications once as a list.
     */
    suspend fun getMedications(): Result<List<Medication>>

    /**
     * Retrieves a single medication by ID.
     */
    suspend fun getMedication(medicationId: String): Result<Medication?>

    /**
     * Inserts or upserts a medication.
     * @return [Result.success] containing the generated or assigned medication ID on success.
     */
    suspend fun addMedication(medication: Medication): Result<String>

    /**
     * Updates an existing medication entity.
     */
    suspend fun updateMedication(medication: Medication): Result<Unit>

    /**
     * Deletes a medication by its unique ID.
     */
    suspend fun deleteMedication(medicationId: String): Result<Unit>

    fun getTodayIntakes(): Flow<List<Medication>>
}
