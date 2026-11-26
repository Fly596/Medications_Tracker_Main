package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.core.database.dao.IntakeDao
import com.galeria.medicationstracker.data.medications.mapper.toDomain
import com.galeria.medicationstracker.data.medications.mapper.toEntity
import com.galeria.medicationstracker.domain.medications.model.Intake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface IntakeRepository {

    suspend fun addIntake(intake: Intake): Result<String>

    suspend fun getIntake(intakeId: String): Result<Intake?>

    fun observeIntakes(): Flow<List<Intake>>

    suspend fun getIntakesForMedication(medicationId: String): Result<List<Intake>>
}

class IntakeRepositoryImpl
@Inject constructor(private val intakeDao: IntakeDao) : IntakeRepository {

    override suspend fun addIntake(intake: Intake): Result<String> {
        if (intake.id.isBlank()) {
            return Result.failure(IllegalArgumentException("Intake ID cannot be blank"))
        }
        intakeDao.upsert(intake.toEntity())
        return Result.success(intake.id)
    }

    override suspend fun getIntake(intakeId: String): Result<Intake?> {
        val intakeEntity = intakeDao.getIntakeById(intakeId)
        return if (intakeEntity != null) {
            Result.success(intakeEntity.toDomain())
        } else {
            Result.success(null)
        }
    }

    override fun observeIntakes(): Flow<List<Intake>> {
        return intakeDao.observeIntakes().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getIntakesForMedication(medicationId: String): Result<List<Intake>> {
        val intakeEntities = intakeDao.getIntakesForMedication(medicationId)
        return Result.success(intakeEntities.map { it.toDomain() })
    }
}