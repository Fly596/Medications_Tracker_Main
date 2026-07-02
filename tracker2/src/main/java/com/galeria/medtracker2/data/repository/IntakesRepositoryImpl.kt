package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.IntakeDao
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.IntakeLogDomain
import com.galeria.medtracker2.domain.repository.IntakesRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IntakesRepositoryImpl @Inject constructor(private val intakeDao: IntakeDao) :
    IntakesRepository {

    override suspend fun addIntake(intake: IntakeLogDomain) {
        if (intakeDao.existsByPlannedId(intake.plannedIntakeId)) {
            intakeDao.updateStatus(status = intake.isTaken, plannedId = intake.plannedIntakeId)
        } else {
            intakeDao.upsert(intake.toEntity())
        }
    }

    override fun getIntakes(): Flow<List<IntakeLogDomain>> = callbackFlow {
        val intakes =
            intakeDao.getAllIntakes().map { intakesList ->
                intakesList.map { intakeEntity ->
                    intakeEntity.toDomain()
                }
            }
        intakes.collect { intakesList ->
            trySend(intakesList)
        }
        awaitClose {}
    }
}
