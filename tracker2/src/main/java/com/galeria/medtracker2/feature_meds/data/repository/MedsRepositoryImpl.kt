package com.galeria.medtracker2.feature_meds.data.repository

import com.galeria.medtracker2.feature_meds.data.source.local.MedicationDao
import com.galeria.medtracker2.feature_meds.data.source.local.MedicationEntity
import com.galeria.medtracker2.feature_meds.domain.MedsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

// TODO: add implementation
class MedsRepositoryImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val medicationDao: MedicationDao) :
    MedsRepository {

    private val medicationCollection = firestore.collection("medications")

    override suspend fun addMedication(
        name: String,
        doseMg: Double?,
        stock: Double?,
        stockMeasureUnit: String?,
        drugClass: String?
    ) {
        val id = UUID.randomUUID().toString()
        val newMedication =
            MedicationEntity(
                id = id,
                name = name,
                doseMg = doseMg,
                stock = stock,
                stockMeasureUnit = stockMeasureUnit,
                drugClass = drugClass)

        medicationDao.insertMedication(newMedication)
        medicationCollection.document(id).set(newMedication).await()
    }

    override suspend fun removeMedication(medicationId: String) {
        try {
            medicationDao.deleteMedicationById(medicationId)
            medicationCollection.document(medicationId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getMedication(medicationId: String) {
        TODO("Not yet implemented")
    }

    override fun getAllMedications() {
        TODO("Not yet implemented")
    }
}
