package com.galeria.medtracker2.feature.meds.data.repository

import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationDao
import com.galeria.medtracker2.feature.meds.data.local.medication.toDomain
import com.galeria.medtracker2.feature.meds.data.local.medication.toEntity
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

// TODO: add implementation
class MedsRepositoryImpl
@Inject
constructor(
    private val medicationDao: MedicationDao
) : MedsRepository {

    override suspend fun addMedication(
        medication: MedicationDomain
    ) {
        medicationDao.insertMedication(medication.toEntity())
    }

    override suspend fun removeMedication(medicationId: UUID) {
        try {
            medicationDao.deleteMedicationById(medicationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getMedication(medicationId: UUID): MedicationDomain {
        val medicationEntity = medicationDao.getMedicationById(medicationId)
        return medicationEntity?.toDomain() ?: throw Exception("Medication not found")
    }

    override fun getAllMedications(): Flow<List<MedicationDomain>> = callbackFlow {
        val meds = medicationDao.getAllMedications().map { entitiesList ->
            entitiesList.map { entity ->
                entity.toDomain()
            }
        }
        meds.collect { medsList ->
            trySend(medsList)
        }
        awaitClose { }
    }
}