package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.IntakeDao
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.IntakeLogDomain
import com.galeria.medtracker2.domain.repository.IntakesRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
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

    override fun getIntakes(): Flow<List<IntakeLogDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIntakeStatus(plannedIntakeId: UUID): Boolean =
        intakeDao.getStatusByPlannedId(plannedIntakeId)
}