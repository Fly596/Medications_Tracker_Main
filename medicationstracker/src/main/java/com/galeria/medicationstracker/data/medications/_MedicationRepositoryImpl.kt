package com.galeria.medicationstracker.data.medications

import com.galeria.medicationstracker.core.database.dao.MedicationDao
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.domain.model.Medication
import com.galeria.medicationstracker.core.domain.repository._MedicationRepository
import com.galeria.medicationstracker.core.firebase.datasource.MedicationDataSource
import com.galeria.medicationstracker.data.toDocument
import com.galeria.medicationstracker.data.toDomain
import com.galeria.medicationstracker.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class _MedicationRepositoryImpl @Inject constructor(
    private val medicationRemote: MedicationDataSource,
    private val medicationDao: MedicationDao
) : _MedicationRepository {

    override suspend fun addMedication(medication: Medication): Result<String> {
        return try {
            // 1. Добавляем в Firestore и получаем DocumentId.
            val documentId = medicationRemote.addMedication(medication.toDocument())
            val identifiedMedication = medication.copy(id = documentId)

            // 2. Добавляем в БД.
            val medDaysPair = identifiedMedication.toEntity()
            medicationDao.upsert(medDaysPair)

            Result.success(documentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeMedication(medicationId: String): Flow<Medication?> =
        medicationDao.getMedicationByIdFlow(medicationId).map { it.toDomain() }

    override fun getMedicationsFlow(): Flow<List<Medication>> =
        medicationDao.observeMedications().map {
            it.map(MedicationEntity::toDomain)
        }

    override suspend fun getMedication(
        medicationId: String
    ): Result<Medication?> {
        TODO("Not yet implemented")
    }

    override suspend fun getMedications(): List<Medication> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMedication(medication: Medication) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMedication(medicationId: String) {
        TODO("Not yet implemented")
    }
}