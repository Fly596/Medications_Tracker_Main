package com.galeria.medicationstracker.data.medications

import com.galeria.medicationstracker.core.database.dao.MedicationDao
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.domain.repository._MedicationRepository
import com.galeria.medicationstracker.core.firebase.datasource.MedicationDataSource
import com.galeria.medicationstracker.data.toDomain
import com.galeria.medicationstracker.data.toEntity
import com.galeria.medicationstracker.domain.medications.model.Medication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class _MedicationRepositoryImpl @Inject constructor(
    private val medicationRemote: MedicationDataSource,
    private val medicationDao: MedicationDao
) : _MedicationRepository {

    override suspend fun addMedication(medication: Medication): Result<String> {

        return try {
            val dbMedication = MedicationEntity(
                id = UUID.randomUUID().toString(),
                name = medication.name,
                dosage = medication.dosage,
                form = medication.form.name,
                startDate = medication.startDate,
                endDate = medication.endDate,
                daysOfWeek = medication.daysOfWeek,
                intakeTime = medication.intakeTime
            )
            medicationDao.upsert(dbMedication)

            Result.success(dbMedication.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /*    override suspend fun addMedication(medication: Medication): Result<String> {
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
        }*/

    override fun observeMedication(medicationId: String): Flow<Medication?> =
        medicationDao.getMedicationByIdFlow(medicationId).map { it?.toDomain() }

    override fun getMedicationsFlow(): Flow<List<Medication>> =
        medicationDao.observeMedications().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getMedication(
        medicationId: String
    ): Result<Medication?> {
        return runCatching {
            medicationDao.getMedicationById(medicationId)?.toDomain()
        }
    }

    override suspend fun getMedications(): List<Medication> {
        return try {
            medicationDao.getMedicationById("") // or fetch list
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateMedication(medication: Medication) {
        medicationDao.upsert(medication.toEntity())
    }

    override suspend fun deleteMedication(medicationId: String) {
        medicationDao.deleteMedicationById(medicationId)
    }
}