package com.galeria.medtracker2.feature.meds.data.local.intakes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface MedicationRegimenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationRegimen(medicationRegimen: MedicationRegimenEntity)

    @Query("DELETE FROM medications_regimens WHERE id = :id")
    suspend fun deleteMedicationRegimenById(id: Uuid)

    @Query("SELECT * FROM medications_regimens")
    fun getAllMedicationRegimens(): Flow<List<MedicationRegimenEntity>>

    @Query("SELECT * FROM medications_regimens WHERE id = :id")
    suspend fun getMedicationRegimenById(id: Uuid): MedicationRegimenEntity?
}
