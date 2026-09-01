package com.galeria.medtracker2.data.repository

import com.galeria.medtracker2.core.database.dao.MainIntakeDao
import com.galeria.medtracker2.data.mappers.toDomain
import com.galeria.medtracker2.data.mappers.toEntity
import com.galeria.medtracker2.domain.model.IntakeDomain
import com.galeria.medtracker2.domain.repository.MainIntakesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainIntakesRepositoryImpl @Inject constructor(private val intakeDao: MainIntakeDao) :
    MainIntakesRepository {

    override suspend fun insertMainIntake(intake: IntakeDomain): Long {
        return intakeDao.insertMainIntake(intake.toEntity())
    }

    override fun getAllMainIntakes(): Flow<List<IntakeDomain>> {
        return intakeDao.getAllMainIntakes().map { entitiesList ->
            entitiesList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteMainIntakeById(intakeId: Int) {
        intakeDao.deleteMainIntakeById(intakeId)
    }

    override suspend fun getMainIntakeById(intakeId: Int): IntakeDomain? {
        return intakeDao.getMainIntakeById(intakeId)?.toDomain()
    }
}