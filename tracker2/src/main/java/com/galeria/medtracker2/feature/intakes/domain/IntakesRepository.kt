package com.galeria.medtracker2.feature.intakes.domain

import com.galeria.medtracker2.feature.intakes.data.local.IntakeDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IntakesRepository {

    suspend fun addIntake(intake: IntakeDomain)

    fun getIntakes(): Flow<List<IntakeDomain>>
}


class IntakesRepositoryImpl @Inject constructor(
    private val intakeDao: IntakeDao
) : IntakesRepository {

    override suspend fun addIntake(intake: IntakeDomain) {
        TODO("Not yet implemented")
    }

    override fun getIntakes(): Flow<List<IntakeDomain>> {
        TODO("Not yet implemented")
    }

}