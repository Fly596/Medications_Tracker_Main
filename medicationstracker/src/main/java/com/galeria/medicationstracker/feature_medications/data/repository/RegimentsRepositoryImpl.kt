package com.galeria.medicationstracker.feature_medications.data.repository

import com.galeria.medicationstracker.feature_medications.domain.model.Regiments
import com.galeria.medicationstracker.feature_medications.domain.repository.RegimentsRepository
import com.galeria.medicationstracker.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegimentsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : RegimentsRepository {
    
    override fun getRegiments(): Flow<Response<List<Regiments>>> {
        TODO("Not yet implemented")
    }
    
    override fun getRegiment(regimentId: String): Flow<Response<Regiments>> {
        TODO("Not yet implemented")
    }
    
    override suspend fun addRegiment(regiment: Regiments): Response<Unit> {
        TODO("Not yet implemented")
    }
    
    override suspend fun deleteRegiment(id: String): Response<Unit> {
        TODO("Not yet implemented")
    }
    
    override suspend fun updateRegiment(regiment: Regiments): Response<Unit> {
        TODO("Not yet implemented")
    }
    
}