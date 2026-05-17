package com.galeria.medtracker2.feature.intakes.data

import com.galeria.medtracker2.feature.intakes.data.local.IntakeDao
import com.galeria.medtracker2.feature.intakes.data.local.toEntity
import com.galeria.medtracker2.feature.intakes.domain.IntakeLogDomain
import com.galeria.medtracker2.feature.intakes.domain.IntakesRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class IntakesRepositoryImpl @Inject constructor(private val intakeDao: IntakeDao) :
    IntakesRepository {

    override suspend fun addIntake(intake: IntakeLogDomain) {
        if (intakeDao.checkIntakeExist(intake.plannedIntakeId)) {
            intakeDao.update(intakeStatus = intake.isTaken, id = intake.plannedIntakeId)
        } else {
            intakeDao.insertIntake(intake.toEntity())
        }
    }

    override fun getIntakes(): Flow<List<IntakeLogDomain>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIntakeStatus(plannedIntakeId: UUID): Boolean =
        intakeDao.checkIntakeStatus(plannedIntakeId)
}
