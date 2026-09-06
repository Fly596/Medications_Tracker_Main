package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.IntakeDao
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.IntakeDomain
import com.galeria.medtracker2.domain.repository.IntakesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class IntakesRepositoryImpl @Inject constructor(private val intakeDao: IntakeDao) :
    IntakesRepository {

    override suspend fun insertIntake(intake: IntakeDomain): Long {
        return intakeDao.insert(intake.toEntity())
    }

    override fun getAllIntakes(): Flow<List<IntakeDomain>> {
        return intakeDao.observeAll().map { entitiesList ->
            entitiesList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteIntakeById(intakeId: Long) {
        intakeDao.deleteById(intakeId)
    }

    override suspend fun getIntakeById(intakeId: Long): IntakeDomain? {
        return intakeDao.getById(intakeId)?.toDomain()
    }

    override fun getTotalDosage(medicationId: UUID): Flow<Double> {
        return intakeDao.getTotalDosage(medicationId)
    }
}