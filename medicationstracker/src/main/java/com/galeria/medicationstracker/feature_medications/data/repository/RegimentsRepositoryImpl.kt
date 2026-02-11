package com.galeria.medicationstracker.feature_medications.data.repository

import com.galeria.medicationstracker.feature_medications.data.source.remote.model.RegimentsDto
import com.galeria.medicationstracker.feature_medications.domain.model.Regiments
import com.galeria.medicationstracker.feature_medications.domain.repository.RegimentsRepository
import com.galeria.medicationstracker.feature_medications.utils.toDomain
import com.galeria.medicationstracker.feature_medications.utils.toDto
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
class RegimentsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher // Всегда инжекть диспетчер!
) : RegimentsRepository {
    
    private val userId: String
        get() = auth.currentUser?.uid
            ?: throw IllegalStateException("User must be logged in to access medications.")
    
    companion object {
        
        private const val USERS_COLLECTION = "User"
        private const val REGIMENTS_SUBCOLLECTION = "regiments"
    }
    
    val regimentsRef =
        firestore
            .collection(USERS_COLLECTION)
            .document(userId.toString())
            .collection(REGIMENTS_SUBCOLLECTION)
    
    override fun getRegiments(): Flow<Response<List<Regiments>>> =
        callbackFlow {
            val subscription =
                regimentsRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Response.Error(error.message.toString()))
                        return@addSnapshotListener
                    }
                    val regiments =
                        snapshot?.documents?.mapNotNull { doc ->
                            doc.toObject(RegimentsDto::class.java)
                                ?.copy(id = doc.id)
                                ?.toDomain()
                        } ?: emptyList()
                    trySend(Response.Success(regiments))
                }
            awaitClose { subscription.remove() }
        }.flowOn(dispatcher)
    
    
    override fun getRegiment(regimentId: String): Flow<Response<Regiments>> {
        TODO("Not yet implemented")
    }
    
    override suspend fun addRegiment(regiment: Regiments): Response<Unit> =
        withContext(dispatcher) {
            runCatching {
                val dto = regiment.toDto()
                if (dto.id.isEmpty()) {
                    regimentsRef.add(dto).await()
                } else {
                    regimentsRef.document(dto.id).set(dto).await()
                }
                Response.Success(Unit)
            }
                .getOrElse {
                    Response.Error(it.message.toString())
                }
        }
    
    override suspend fun deleteRegiment(id: String): Response<Unit> {
        TODO("Not yet implemented")
    }
    
    override suspend fun updateRegiment(regiment: Regiments): Response<Unit> {
        TODO("Not yet implemented")
    }
    
}