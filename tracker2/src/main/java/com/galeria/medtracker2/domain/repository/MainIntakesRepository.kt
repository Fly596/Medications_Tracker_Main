package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.IntakeDomain
import kotlinx.coroutines.flow.Flow

interface MainIntakesRepository {

    suspend fun insertMainIntake(intake: IntakeDomain): Long
    fun getAllMainIntakes(): Flow<List<IntakeDomain>>
    suspend fun deleteMainIntakeById(intakeId: Int)
    suspend fun getMainIntakeById(intakeId: Int): IntakeDomain?
}

