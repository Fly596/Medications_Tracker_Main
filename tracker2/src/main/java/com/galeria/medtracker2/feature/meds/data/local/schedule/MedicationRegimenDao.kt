package com.galeria.medtracker2.feature.meds.data.local.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Dao
interface MedicationRegimenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationRegimen(medicationRegimen: MedicationRegimenEntity)

    @Query("DELETE FROM medications_regimens WHERE id = :id")
    suspend fun deleteMedicationRegimenById(id: UUID)

    @Query("SELECT * FROM medications_regimens")
    fun getAllMedicationRegimens(): Flow<List<MedicationRegimenEntity>>

    @Query("SELECT * FROM medications_regimens WHERE id = :id")
    suspend fun getMedicationRegimenById(id: UUID): MedicationRegimenEntity?

    @Query(
        """
            SELECT m.name, mr.doseMg, mr.startDate, mr.endDate
            FROM medications AS m JOIN medications_regimens AS mr
            ON m.id = mr.medicationId
            """
    )
    fun getRegimentWithNameDoseDates(): Flow<List<RegimentWithNameDoseDate>>

    @Query(
        """
            SELECT std.id, m.name, mr.doseMg, std.scheduledIntakeDateTime
            FROM medications AS m
            JOIN medications_regimens AS mr ON m.id = mr.medicationId
            LEFT JOIN scheduled_date_times AS std ON std.medicationScheduleId = mr.id
            """
    )
    fun getFullScheduleDateTimes(): Flow<List<FullSchedule>>
}

data class RegimentWithNameDoseDate(
    val name: String,
    val doseMg: Double,
    val startDate: Long,
    val endDate: Long
)

data class FullSchedule(
    val id: UUID,
    val name: String,
    val doseMg: Double,
    val scheduledIntakeDateTime: Long,
)
