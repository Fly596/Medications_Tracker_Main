package com.galeria.medtracker2.feature.meds.data

import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationDao
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

// TODO: add implementation
class MedsRepositoryImpl
@Inject
constructor(
    private val medicationDao: MedicationDao
) :
    MedsRepository {

    override suspend fun addMedication(
        medication: MedicationDomain
    ) {

        val medicationEntity = medication.toEntity()
        medicationDao.insertMedication(medicationEntity)
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

    override fun getAllMedications(): Flow<List<MedicationDomain>> {
        return medicationDao.getAllMedications().map { it.toDomain() }
    }
}