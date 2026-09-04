package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.IntakeDomain
import kotlinx.coroutines.flow.Flow

interface IntakesRepository {

    suspend fun insertMainIntake(intake: IntakeDomain): Long
    fun getAllMainIntakes(): Flow<List<IntakeDomain>>
    suspend fun deleteMainIntakeById(intakeId: Long)
    suspend fun getMainIntakeById(intakeId: Long): IntakeDomain?
}

