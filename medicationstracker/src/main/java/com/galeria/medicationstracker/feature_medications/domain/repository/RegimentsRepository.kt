package com.galeria.medicationstracker.feature_medications.domain.repository

import com.galeria.medicationstracker.feature_medications.domain.model.Regiments
import com.galeria.medicationstracker.utils.Response
import kotlinx.coroutines.flow.Flow

interface RegimentsRepository {
    
    fun getRegiments(): Flow<Response<List<Regiments>>>
    
    fun getRegiment(regimentId: String): Flow<Response<Regiments>>
    
    suspend fun addRegiment(regiment: Regiments): Response<Unit>
    
    suspend fun deleteRegiment(id: String): Response<Unit>
    
    suspend fun updateRegiment(regiment: Regiments): Response<Unit>
    
    
}