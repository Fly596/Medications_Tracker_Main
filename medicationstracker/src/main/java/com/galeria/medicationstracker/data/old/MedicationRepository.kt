package com.galeria.medicationstracker.data.old
/*

interface MedicationRepository {

    suspend fun addMedication(medication: Medication)
    suspend fun addInteraction(interaction: Interaction)
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

    override suspend fun addInteraction(interaction: Interaction) {
        val data = hashMapOf(
            "medication1" to interaction.medication1,
            "medication2" to interaction.medication2,
            "effect" to interaction.effect,
            "severity" to interaction.severity
        )
        val docId = "${interaction.medication1}_${interaction.medication2}"
        firestore.collection("Interactions").document(docId).set(data)
            .addOnCompleteListener {
                println("Interaction added")
            }
            .addOnFailureListener { e ->
                println("Обосрался с записью: $e")
            }
    }
}
*/
