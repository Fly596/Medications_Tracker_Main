package com.galeria.medicationstracker.data.medications

import com.galeria.medicationstracker.core.database.dao.MedicationDao
import com.galeria.medicationstracker.core.domain.repository.MedicationRepository
import com.galeria.medicationstracker.core.firebase.datasource.MedicationDataSource
import com.galeria.medicationstracker.data.toDomain
import com.galeria.medicationstracker.data.toEntity
import com.galeria.medicationstracker.domain.medications.model.Medication
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val medicationRemote: MedicationDataSource,
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override fun getMedicationsFlow(): Flow<List<Medication>> =
        medicationDao.observeMedications().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getMedications(): Result<List<Medication>> =
        runCatchingCancellable {
            medicationDao.getMedications().map { it.toDomain() }
        }

    override suspend fun getMedication(medicationId: String): Result<Medication?> =
        runCatchingCancellable {
            medicationDao.getMedicationById(medicationId)?.toDomain()
        }

    override suspend fun addMedication(medication: Medication): Result<String> =
        runCatchingCancellable {
            val medWithId = if (medication.id.isBlank()) {
                medication.copy(id = UUID.randomUUID().toString())
            } else {
                medication
            }
            val dbMedication = medWithId.toEntity()
            medicationDao.upsert(dbMedication)
            dbMedication.id
        }

    override suspend fun updateMedication(medication: Medication): Result<Unit> =
        runCatchingCancellable {
            medicationDao.upsert(medication.toEntity())
        }

    override suspend fun deleteMedication(medicationId: String): Result<Unit> =
        runCatchingCancellable {
            medicationDao.deleteMedicationById(medicationId)
        }

    override fun getTodayIntakes(): Flow<List<Medication>> {
        val today = LocalDate.now()
        val dayOfWeek = today.dayOfWeek.name
        return medicationDao.getMedicationsForDate(today, dayOfWeek).map { list ->
            list.map { it.toDomain() }
        }
    }

    /**
     * Inline helper function to execute suspending blocks wrapped in [Result],
     * while preserving coroutine structured concurrency by rethrowing [CancellationException].
     */
    private inline fun <T> runCatchingCancellable(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}