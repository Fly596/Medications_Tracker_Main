package com.galeria.medicationstracker.feature_medications.data.repository

import com.galeria.medicationstracker.feature_medications.data.source.local.MedicationDao
import com.galeria.medicationstracker.feature_medications.data.source.remote.model.MedicationDto
import com.galeria.medicationstracker.feature_medications.domain.model.Medication
import com.galeria.medicationstracker.feature_medications.domain.repository.MedicationRepository
import com.galeria.medicationstracker.feature_medications.utils.toDomain
import com.galeria.medicationstracker.feature_medications.utils.toDto
import com.galeria.medicationstracker.feature_medications.utils.toEntity
import com.galeria.medicationstracker.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher, // Всегда инжекть диспетчер!
    private val medicationDao: MedicationDao,
) : MedicationRepository {
    
    private val userId: String
        get() = auth.currentUser?.uid
            ?: throw IllegalStateException("User must be logged in to access medications.")
    
    companion object {
        
        private const val USERS_COLLECTION = "User"
        private const val MEDICATIONS_SUBCOLLECTION = "medications"
    }
    
    val medicationsRef =
        firestore
            .collection(USERS_COLLECTION)
            .document(userId.toString())
            .collection(MEDICATIONS_SUBCOLLECTION)
    
    override fun getMedications(): Flow<Response<List<Medication>>> =
        callbackFlow {
            val subscription =
                medicationsRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Response.Error(error.message.toString()))
                        return@addSnapshotListener
                    }
                    val meds =
                        snapshot?.documents?.mapNotNull { doc ->
                            doc.toObject(MedicationDto::class.java)
                                ?.copy(id = doc.id)
                                ?.toDomain()
                        } ?: emptyList()
                    
                    trySend(Response.Success(meds))
                }
            awaitClose { subscription.remove() }
        }
            .flowOn(dispatcher)
    
    override fun getMedication(medicationId: String): Flow<Response<Medication>> =
        callbackFlow {
            val subscription =
                medicationsRef.document(medicationId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(Response.Error(error.message.toString()))
                            return@addSnapshotListener
                        }
                        val medication =
                            snapshot?.toObject(MedicationDto::class.java)
                        if (medication == null) {
                            trySend(Response.Error("Medication not found"))
                        } else {
                            trySend(Response.Success(medication.toDomain()))
                        }
                    }
            awaitClose { subscription.remove() }
        }
            .flowOn(dispatcher)
    
    // override suspend fun addMedication(medication: Medication): Response<Unit> =
    //     withContext(dispatcher) {
    //         runCatching {
    //             val dto = medication.toDto()
    //             if (dto.id.isEmpty()) {
    //                 medicationsRef.add(dto).await()
    //             } else {
    //                 medicationsRef.document(dto.id).set(dto).await()
    //             }
    //             Response.Success(Unit)
    //         }
    //             .getOrElse { Response.Error(it.message.toString()) }
    //     }
    override suspend fun addMedication(medication: Medication): Response<Unit> {
        val medicationEntity = medication.toEntity()
        
        medicationDao.insertMedication(medicationEntity)
        medicationsRef.document(medicationEntity.id)
            .set(medicationEntity.toDto()).await()
        return Response.Success(Unit)
    }
    
    override suspend fun deleteMedication(id: String): Response<Unit> =
        withContext(dispatcher) {
            runCatching {
                medicationsRef.document(id).delete().await()
                Response.Success(Unit)
            }
                .getOrElse { Response.Error(it.message.toString()) }
        }
    
    override suspend fun updateMedication(medication: Medication): Response<Unit> {
        TODO("Not yet implemented")
    }
}