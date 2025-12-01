package com.galeria.medicationstracker.domain.repository

import com.galeria.medicationstracker.data.DomainIntake
import com.galeria.medicationstracker.domain.model.IntakeDomain
import kotlinx.coroutines.flow.Flow

interface IntakeRepository{
    fun getUserIntakes(userId: Int): Flow<List<DomainIntake>>
    
    fun addIntake(intake: IntakeDomain)
}