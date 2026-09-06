package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.IntakeDomain
import kotlinx.coroutines.flow.Flow

interface IntakesRepository {

    suspend fun insertIntake(intake: IntakeDomain): Long
    fun getAllIntakes(): Flow<List<IntakeDomain>>
    suspend fun deleteIntakeById(intakeId: Long)
    suspend fun getIntakeById(intakeId: Long): IntakeDomain?
}

