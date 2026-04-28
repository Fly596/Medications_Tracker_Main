package com.galeria.medtracker2.feature.intakes.domain

import kotlinx.coroutines.flow.Flow

interface IntakesRepository {

    suspend fun addIntake(intake: IntakeLogDomain)

    fun getIntakes(): Flow<List<IntakeLogDomain>>
}