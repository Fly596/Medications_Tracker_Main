package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.MedicationDao
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.repository.MedicationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

// TODO: add implementation
class MedicationRepositoryImpl
@Inject
constructor(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override suspend fun addMedication(
        medication: MedicationDomain
    ) {
        medicationDao.insert(medication.toEntity())
    }

    override suspend fun getMedicationById(medicationId: UUID): MedicationDomain? {
        medicationDao.getById(medicationId)?.let { entity ->
            return entity.toDomain()
        }
        return null
    }

    override suspend fun removeMedication(medicationId: UUID) {
        try {
            medicationDao.deleteById(medicationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getMedicationByName(name: String): MedicationDomain? {
        val medicationEntity = medicationDao.getByName(name)
        return medicationEntity?.toDomain()
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