package com.galeria.medtracker2.feature.meds.data

import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationDao
import com.galeria.medtracker2.feature.meds.data.local.medication.MedicationEntity
import com.galeria.medtracker2.feature.meds.domain.DomainMedication
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import kotlinx.coroutines.flow.Flow
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
        name: String,
        doseMg: Double?,
    ) {
        val id = UUID.randomUUID().toString()
        val newMedication =
            MedicationEntity(
                id = id,
                name = name,
                doseMg = doseMg,
            )

        medicationDao.insertMedication(newMedication)
    }

    override suspend fun removeMedication(medicationId: String) {
        try {
            medicationDao.deleteMedicationById(medicationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getMedication(medicationId: String): DomainMedication {
        TODO("Not yet implemented")
    }

    override fun getAllMedications(): Flow<List<DomainMedication>> {
        TODO("Not yet implemented")
    }
}