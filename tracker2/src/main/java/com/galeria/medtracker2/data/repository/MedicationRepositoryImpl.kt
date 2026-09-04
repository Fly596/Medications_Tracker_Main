package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.MedicationDao
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

// TODO: add implementation
class MedicationRepositoryImpl
@Inject
constructor(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override fun observeMedications(): Flow<List<MedicationDomain>> {
        return medicationDao
            .getAllMedications()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun addMedication(
        medication: MedicationDomain
    ) {
        medicationDao.insert(medication.toEntity())
    }

    override suspend fun getMedication(medicationId: UUID): MedicationDomain? {
        return medicationDao.getById(medicationId)?.toDomain()
    }

    override suspend fun updateMedication(medication: MedicationDomain) {
        medicationDao.update(medication.toEntity())
    }

    override suspend fun removeMedication(medicationId: UUID) {
        try {
            medicationDao.deleteById(medicationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}