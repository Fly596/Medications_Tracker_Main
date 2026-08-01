package com.galeria.medicationstracker.data._repository

import com.galeria.medicationstracker.core.database.dao.MedicationDao
import com.galeria.medicationstracker.core.database.entity.MedicationWithDays
import com.galeria.medicationstracker.core.domain.model.Medication
import com.galeria.medicationstracker.core.domain.repository._MedicationRepository
import com.galeria.medicationstracker.core.firebase.datasource.MedicationDataSource
import com.galeria.medicationstracker.data.toDocument
import com.galeria.medicationstracker.data.toDomain
import com.galeria.medicationstracker.data.toRoomEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class _MedicationRepositoryImpl @Inject constructor(
  private val medicationRemote: MedicationDataSource,
  private val medicationDao: MedicationDao
) : _MedicationRepository {

  companion object {

    private const val USERS_COLLECTION = "User"
    private const val MEDICATIONS_SUBCOLLECTION = "medications"
  }

  override suspend fun addMedication(userId: String, medication: Medication): Result<String> {
    return try {
      // 1. Добавляем в Firestore и получаем DocumentId.
      val documentId = medicationRemote.addMedication(userId, medication.toDocument())
      val identifiedMedication = medication.copy(id = documentId).toDocument()

      // 2. Добавляем в БД.
      val medDaysPair = identifiedMedication.toRoomEntities()
      medicationDao.insertMedicationWithDays(medDaysPair.first, medDaysPair.second)

      Result.success(documentId)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override fun getMedication(medicationId: String): Flow<Medication?> =
      medicationDao.getMedicationWithDays(medicationId).map { it.toDomain() }

  override fun getMedicationsFlow(userId: String): Flow<List<Medication>> =
      medicationDao.getAllMedicationsWithDays().map {
        it.map(MedicationWithDays::toDomain)
      }

  override suspend fun getMedications(userId: String): List<Medication> {
    TODO("Not yet implemented")
  }

  override suspend fun updateMedication(userId: String, medication: Medication) {
    TODO("Not yet implemented")
  }

  override suspend fun deleteMedication(userId: String, medicationId: String) {
    TODO("Not yet implemented")
  }
}