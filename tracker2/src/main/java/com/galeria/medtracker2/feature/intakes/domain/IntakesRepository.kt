package com.galeria.medtracker2.feature.intakes.domain

import kotlinx.coroutines.flow.Flow

interface IntakesRepository {

    suspend fun addIntake(intake: IntakeDomain)

    fun getIntakes(): Flow<List<IntakeDomain>>
}