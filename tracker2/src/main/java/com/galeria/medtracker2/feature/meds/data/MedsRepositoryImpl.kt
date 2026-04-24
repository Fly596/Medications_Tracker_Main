package com.galeria.medtracker2.feature.meds.data

import com.galeria.medtracker2.feature.meds.data.local.MedicationDao
import com.galeria.medtracker2.feature.meds.data.local.MedicationEntity
import com.galeria.medtracker2.feature.meds.domain.DomainMedication
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

// TODO: add implementation
class MedsRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val medicationDao: MedicationDao
) :
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
                drugClass = drugClass
            )
        
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
    
    override suspend fun getMedication(medicationId: String): DomainMedication {
        TODO("Not yet implemented")
    }
    
    override fun getAllMedications(): Flow<List<DomainMedication>> {
        TODO("Not yet implemented")
    }
}