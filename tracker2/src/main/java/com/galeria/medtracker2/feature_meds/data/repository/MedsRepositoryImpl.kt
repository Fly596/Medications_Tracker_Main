package com.galeria.medtracker2.feature_meds.data.repository

import com.galeria.medtracker2.feature_meds.domain.MedsRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MedsRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
) : MedsRepository {

    override suspend fun addMedication(
        name: String,
        doseMg: Double?,
        stock: Double?,
        stockMeasureUnit: String?,
        drugClass: String?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeMedication(medicationId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getMedication(medicationId: String) {
        TODO("Not yet implemented")
    }

    override fun getAllMedications() {
        TODO("Not yet implemented")
    }
}
