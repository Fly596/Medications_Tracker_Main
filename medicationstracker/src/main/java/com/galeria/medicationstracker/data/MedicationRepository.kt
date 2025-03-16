package com.galeria.medicationstracker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

interface MedicationRepository {
    
    suspend fun addMedication(medication: Medication)
}

class MedicationRepositoryImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) :
    MedicationRepository {
    
    private val medicationsCollection = firestore.collection("Medications")
    
    
    override suspend fun addMedication(medication: Medication) {
        firestore.collection("Medications").add(medication)
            .addOnCompleteListener {
                println("Medication added")
            }
    }
}
