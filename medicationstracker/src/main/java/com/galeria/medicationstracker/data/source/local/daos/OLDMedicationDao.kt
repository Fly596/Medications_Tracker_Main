package com.galeria.medicationstracker.data.source.local.daos
/*@Dao
interface OLDMedicationDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMedication(medication: Medication): Long

    @Update suspend fun updateMedication(medication: Medication)

    @Delete suspend fun deleteMedication(medication: Medication)

    @Query("SELECT * FROM medication WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getMedicationByFirestoreId(firestoreId: String): Medication?

    @Query("SELECT * FROM medication") fun getAllMedications(): Flow<List<Medication>>

    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: Int): Medication?

    @Upsert suspend fun upsertMedication(medication: Medication)

    @Query("DELETE FROM medication WHERE id = :id") suspend fun deleteMedicationById(id: Int)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMedications(medication: Medication)
}*/

/*@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMedication(medication: Medication): Long

    @Update suspend fun updateMedication(medication: Medication)

    @Delete suspend fun deleteMedication(medication: Medication)

    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getMedicationById(id: Int): Medication?

    @Query("SELECT * FROM medication") fun getAllMedications(): Flow<List<Medication>>
}*/
