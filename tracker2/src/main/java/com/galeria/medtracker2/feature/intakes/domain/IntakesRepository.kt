package com.galeria.medtracker2.feature.intakes.domain

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IntakesRepository {

    suspend fun addIntake(intake: IntakeLogDomain)

    fun getIntakes(): Flow<List<IntakeLogDomain>>

    suspend fun checkIntakeStatus(plannedIntakeId: UUID): Boolean
}