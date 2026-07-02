package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.IntakeLogDomain
import kotlinx.coroutines.flow.Flow

interface IntakesRepository {

    suspend fun addIntake(intake: IntakeLogDomain)

    fun getIntakes(): Flow<List<IntakeLogDomain>>
}
