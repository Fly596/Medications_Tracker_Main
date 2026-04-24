package com.galeria.medtracker2.feature.meds.data.local.intakes

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
}
