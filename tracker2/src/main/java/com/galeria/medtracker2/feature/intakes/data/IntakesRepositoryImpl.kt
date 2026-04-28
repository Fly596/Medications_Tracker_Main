package com.galeria.medtracker2.feature.intakes.data

import com.galeria.medtracker2.feature.intakes.data.local.IntakeDao
import com.galeria.medtracker2.feature.intakes.domain.IntakeDomain
import com.galeria.medtracker2.feature.intakes.domain.IntakesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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