package com.galeria.medicationstracker.feature_medications.domain.repository

import com.galeria.medicationstracker.feature_medications.domain.model.Medication
import com.galeria.medicationstracker.utils.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface MedicationRepository {
    suspend fun addMedication(medication: Medication): Response<Unit>

    fun getMedications(): Response<Flow<List<Medication>>>

    fun getMedication(medicationId: String): Response<Flow<Medication>>

    suspend fun deleteMedication(medication: Medication): Response<Unit>

    suspend fun updateMedication(medication: Medication): Response<Unit>
}

@Singleton
class MedicationRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
) : MedicationRepository {

    override suspend fun addMedication(medication: Medication): Response<Unit> {
        TODO("Not yet implemented")
    }

    override fun getMedications(): Response<Flow<List<Medication>>> {
        TODO("Not yet implemented")
    }

    override fun getMedication(medicationId: String): Response<Flow<Medication>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMedication(medication: Medication): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMedication(medication: Medication): Response<Unit> {
        TODO("Not yet implemented")
    }
}
